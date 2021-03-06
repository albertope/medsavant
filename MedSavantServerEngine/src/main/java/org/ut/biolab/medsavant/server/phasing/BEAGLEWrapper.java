package org.ut.biolab.medsavant.server.phasing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import main.Main;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ut.biolab.medsavant.shared.util.DirectorySettings;
import org.ut.biolab.medsavant.shared.util.IOUtils;

/**
 * Call BEAGLE to perform genotype phasing. Multi-patient VCFs are supported.
 *
 * @author rammar
 */
public class BEAGLEWrapper {

    private static final Log LOG = LogFactory.getLog(BEAGLEWrapper.class);
    private static final int FORMAT_COLUMN_INDEX = 8;
    private static final int SAMPLE_COLUMN_INDEX = 9;
    private static final String GT = "GT";

    private static Random r = new Random(); // use static, so it doesn't use the same pseudorandom #s repeatedly

    private File inputVCF;
    private File tempDirectory;
    private String tempFileNamePrefix;
    private List<String> beagleArgs = new LinkedList<String>();
    private Map<String, List<String>> phasedVariants;
    private File mergedVCF;

    private static File refPanel = null;
    private static final String BEAGLE_COMPRESSED_REFERENCE = "CPIC_30k_pgx_phasing_windows_CHR_PREFIX.vcf.bz2";

    private static synchronized void initBeagleReference(File destDir) throws IOException {
        System.out.println("Initializing Phasing reference...");
        LOG.info("Initializing Phasing reference...");
        File tmpDir = null;
        File compressedReference = null;
        try {
            //Copy the compressed reference out of the jar and into a temporary folder.
            int i = 0;
            do {
                tmpDir = new File(DirectorySettings.getTmpDirectory(), "beagle" + i++);
            } while (tmpDir.exists());

            tmpDir.mkdirs();

            compressedReference = new File(tmpDir, BEAGLE_COMPRESSED_REFERENCE);
            InputStream compressedRefStream = BEAGLEWrapper.class.getResourceAsStream("/BEAGLEWrapper/" + BEAGLE_COMPRESSED_REFERENCE);
            IOUtils.copyStream(compressedRefStream, new FileOutputStream(compressedReference));

            //Decompress the reference to its permanent location, then delete the compressed copy.            
            List<File> fileList = IOUtils.decompressAndDelete(compressedReference, destDir);

            if (fileList.size() > 1) {
                LOG.info("WARNING: Unexpected number of files in beagle reference -- expected a single VCF");
            } else if (fileList.size() < 1) {
                throw new IOException("Couldn't locate phasing reference within compressed phasing file.");
            }

            String extractedFileName = fileList.get(0).getCanonicalPath();
            if (!refPanel.getCanonicalPath().equals(extractedFileName)) {
                refPanel.delete();
                throw new IOException("Unexpected filename for phasing reference: " + extractedFileName);
            }
        } finally {
            if (tmpDir != null) {
                IOUtils.deleteDirectory(tmpDir);
            }
        }
        System.out.println("Phasing reference initialized.");
        LOG.info("Phasing reference initialized.");
    }

    public static synchronized void install(File directory) throws IOException {
        File destDir = new File(DirectorySettings.getMedSavantDirectory(), "beagle");
        if (!destDir.exists()) {
            if (!destDir.mkdirs()) {
                throw new IOException("Couldn't create destination directory for phasing reference");
            }
        }
        refPanel = new File(destDir, StringUtils.substringBeforeLast(BEAGLE_COMPRESSED_REFERENCE, ".bz2"));

        if (!refPanel.exists()) {
            LOG.info("Phasing reference doesn't exist, extracting to " + destDir.getAbsolutePath());
            initBeagleReference(destDir);
        }
    }

    /**
     * Perform genotype phasing of the input VCF using BEAGLE.
     *
     * @param tempDirectoryPath The temp directory where files can be processed.
     * @param inputVCF The input VCF name to phase and replace
     * @param numThreads The number of BEAGLE threads
     * @precondition The input VCF and the reference panel must have the same
     * chromosomal nomenclature
     */
    public BEAGLEWrapper(File tempDirectory, File inputVCF, int numThreads) {
        this.inputVCF = inputVCF;
        this.tempDirectory = tempDirectory;

        /* Set the arguments for BEAGLE. */
        tempFileNamePrefix = getPathToTempFile(tempDirectory);

        beagleArgs.add("gt=" + inputVCF.getAbsolutePath());
        beagleArgs.add("ref=" + refPanel.getAbsolutePath());
        beagleArgs.add("out=" + tempFileNamePrefix);
        //beagleArgs.add("window=24000"); // let BEAGLE use default window size, which = 24000
        beagleArgs.add("impute=false"); // need to specify this
        beagleArgs.add("nthreads=" + numThreads);
    }

    /**
     * Perform genotype phasing of the input VCF using Beagle.
     *
     * @return The phased file, located in a temporary directory
     */
    public File run() throws FileNotFoundException, IOException {
        /* Call the main method for BEAGLE. */
        Main.main(beagleArgs.toArray(new String[0]));

        /* Merge the phased VCF results with the unphased results. */
        mergedVCF = mergePhased();
        return mergedVCF;
    }

    /**
     * Perform genotype phasing of the input VCF using BEAGLE using a single
     * thread for BEAGLE run.
     *
     * @param tempDirectoryPath The temp directory where files can be processed.
     * @param inputVCF The input VCF name to phase and replace
     * @precondition The input VCF and the reference panel must have the same
     * chromosomal nomenclature
     */
    public BEAGLEWrapper(File tempDirectory, File inputVCF) {
        this(tempDirectory, inputVCF, 1); // 1 thread
    }

    /**
     * Get a temporary file name.
     *
     * @param tempDirectoryPath The temp directory where files can be processed.
     * @return the temp file name
     */
    private String getPathToTempFile(File tempDirectoryPath) {
        File tempFile = null;
        do {
            tempFile = new File(tempDirectoryPath, "beagle_phasing_temp_" + r.nextInt());

        } while (tempFile.exists());

        return tempFile.getAbsolutePath();
    }

    /**
     * Merge the phased VCF results with the input (unphased) VCF. Works for
     * multi-patient/sample VCF files.
     *
     * @return the new File
     */
    private File mergePhased() throws FileNotFoundException, IOException {        
        // Get the new file name and put it in the temp directory
        File outputVCF = new File(tempDirectory,
                inputVCF.getName().substring(inputVCF.getName().lastIndexOf(File.separator) + 1,
                        inputVCF.getName().lastIndexOf(".")) + "_partially_phased.vcf");

        storePhasedVariants();

        BufferedWriter bw = new BufferedWriter(new FileWriter(outputVCF));
        BufferedReader br = new BufferedReader(new FileReader(inputVCF));
        String line = br.readLine();
        while (line != null) {
            line = StringUtils.chomp(line); // remove terminal newline
            // output header lines without processing
            if (line.substring(0, 1).equals("#")) {
                bw.write(line);
            } else {
                List<String> currentVariants = Arrays.asList(line.split("\\t"));

                /* use chrom, position, ref and alt as a key - these should
                 * be identical between the input and output VCFs. ID (index 2)
                 * is not always present on the input file. */
                List<String> keyList = new LinkedList<String>();
                keyList.addAll(currentVariants.subList(0, 2));
                keyList.addAll(currentVariants.subList(3, 5));
                String key = StringUtils.join(keyList, " ");

                /* Check if this variant was phased. This can depend on whether the
                 * reference panel contained these markers and whether imputation
                 * was enabled. */
                if (phasedVariants.containsKey(key)) {
                    /* Get the phased GT values. */
                    List<String> phasedVariantsLine = phasedVariants.get(key);
                    List<String> phasedFormatColumn = Arrays.asList(phasedVariantsLine.get(FORMAT_COLUMN_INDEX).split(":"));
                    List<List<String>> phasedSampleColumns = getSampleColumns(phasedVariantsLine);
                    int phasedGTIndex = phasedFormatColumn.indexOf(GT);

                    /* Replace the unphased GT values if they exist. Append otherwise. */
                    List<String> unphasedFormatColumn = Arrays.asList(currentVariants.get(FORMAT_COLUMN_INDEX).split(":"));
                    List<List<String>> unphasedSampleColumns = getSampleColumns(currentVariants);
                    int unphasedGTIndex = unphasedFormatColumn.indexOf(GT);

                    /* Assumption: phased and unphased files have the same number
                     * of patient columns. */
                    for (int i = 0; i != phasedSampleColumns.size(); ++i) {
                        List<String> phasedSampleColumn = phasedSampleColumns.get(i);
                        List<String> unphasedSampleColumn = unphasedSampleColumns.get(i);

                        String phasedGTValue = phasedSampleColumn.get(phasedGTIndex);
                        if (unphasedGTIndex > -1) { // GT field is found in format column
                            unphasedSampleColumn.set(unphasedGTIndex, phasedGTValue);
                        } else {
                            if (unphasedGTIndex == -1) { // GT field is missing from format column
                                unphasedGTIndex = -2; // special index to indicate GT is added to first column but not all
                                unphasedFormatColumn.add(GT);
                            }
                            // append GT to the end
                            unphasedSampleColumn.add(phasedGTValue);
                        }

                        /* Replace the GT values in the current columns. */
                        currentVariants.set(SAMPLE_COLUMN_INDEX + i, StringUtils.join(unphasedSampleColumn, ":"));
                    }

                    /* Write the modified line to the new merged VCF. */
                    currentVariants.set(FORMAT_COLUMN_INDEX, StringUtils.join(unphasedFormatColumn, ":"));
                    String outputLine = StringUtils.join(currentVariants, "\t");
                    bw.write(outputLine);
                } else { // variant was not phased
					/* If the input VCF was phased ahead of time, as Complete Genomics files are,
                     * we have to ignore that phasing and use only the phased genotypes from the
                     * BEAGLE output based on the reference panel. Otherwise we'll end up with
                     * potentially non-existent haplotypes. To avoid this, I'm removing all
                     * phasing on variants that were not found in the reference panel (if they
                     * were phased ahead of time). */
                    List<String> unphasedFormatColumn = Arrays.asList(currentVariants.get(FORMAT_COLUMN_INDEX).split(":"));
                    List<List<String>> unphasedSampleColumns = getSampleColumns(currentVariants);
                    int unphasedGTIndex = unphasedFormatColumn.indexOf(GT);

                    /* Iterate through all the sample/patient columns of the VCF. */
                    for (int i = 0; i != unphasedSampleColumns.size(); ++i) {
                        List<String> unphasedSampleColumn = unphasedSampleColumns.get(i);

                        // If GT field is found in format column
                        if (unphasedGTIndex != -1) {
                            /* Replace the phased GT values with unphased ("|" becomes "/"). */
                            String inputGTValue = unphasedSampleColumn.get(unphasedGTIndex);
                            inputGTValue = inputGTValue.replace("|", "/");
                            unphasedSampleColumn.set(unphasedGTIndex, inputGTValue);
                        }

                        /* Replace the GT values in the current columns. */
                        currentVariants.set(SAMPLE_COLUMN_INDEX + i, StringUtils.join(unphasedSampleColumn, ":"));
                    }

                    /* Write the modified line to the new merged VCF. */
                    String outputLine = StringUtils.join(currentVariants, "\t");
                    bw.write(outputLine);
                }
            }
            bw.write("\n"); // we're going to assume this is done in unix rather than using System.getProperty("line.separator")
            line = br.readLine();
        }
        br.close();
        bw.close();

        return outputVCF;
    }

    /**
     * Since BEAGLE outputs a gzipped VCF, uncompress it first and then load
     * each line into memory keyed by the variant positions.
     */
    private void storePhasedVariants() throws FileNotFoundException, IOException {
        phasedVariants = new HashMap<String, List<String>>();

        GZIPInputStream gzippedInputStream = new GZIPInputStream(new FileInputStream(tempFileNamePrefix + ".vcf.gz"));
        BufferedReader br = new BufferedReader(new InputStreamReader(gzippedInputStream));
        String line = br.readLine();
        while (line != null) {
            if (!line.substring(0, 1).equals("#")) { // ignore the header lines
                line = StringUtils.chomp(line); // remove terminal newline
                List<String> currentVariants = Arrays.asList(line.split("\\t"));

                /* use chrom, position, ref and alt as a key - these should
                 * be identical between the input and output VCFs. ID (index 2)
                 * is not always present on the input file. */
                List<String> keyList = new LinkedList<String>();
                keyList.addAll(currentVariants.subList(0, 2));
                keyList.addAll(currentVariants.subList(3, 5));
                String key = StringUtils.join(keyList, " ");
                phasedVariants.put(key, currentVariants);
            }
            line = br.readLine();
        }
        br.close();
    }

    /**
     * Convert the list of current variants (from the complete current line) to
     * a list of sample/patient info lists.
     *
     * @param currentVariants A List representation of the current line's
     * columns
     * @return a List of the sample/patient info columns as Lists (list of
     * lists)
     */
    private List<List<String>> getSampleColumns(List<String> currentVariants) {
        List<List<String>> output = new LinkedList<List<String>>();

        // Get all the sample Columns
        List<String> sampleInfoColumns = currentVariants.subList(SAMPLE_COLUMN_INDEX, currentVariants.size());

        // Iterate through the columns of this line
        for (String column : sampleInfoColumns) {
            output.add(Arrays.asList(column.split(":")));
        }

        return output;
    }

    /**
     * Get the new partially phased merged VCF.
     *
     * @return the merged VCF
     */
    public File getMergedVCF() {
        return this.mergedVCF;
    }

}
