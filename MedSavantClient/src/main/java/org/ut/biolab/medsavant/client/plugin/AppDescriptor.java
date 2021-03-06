/**
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.ut.biolab.medsavant.client.plugin;

import java.io.File;
import java.io.InputStream;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ut.biolab.medsavant.client.plugin.PluginVersionException;
/**
 * Plugin description read from the plugin.xml file.
 *
 * @author tarkvara
 */
public class AppDescriptor implements Comparable<AppDescriptor> {
    private static final Log LOG = LogFactory.getLog(AppDescriptor.class);
    public static class AppVersion implements Comparable {

        private final int minorVersion;
        private final int majorVersion;
        private final int bugfixVersion;

        public AppVersion(String version) {
            String[] s = version.split("\\.", 0);
            try {
                majorVersion = Integer.parseInt(s[0]);
                minorVersion = Integer.parseInt(s[1]);
                bugfixVersion = Integer.parseInt(s[2]);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new AssertionError("Invalid App Version " + version + ". App Versions must be of the format <major>.<minor>.<bugfix>");
            }
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 59 * hash + this.minorVersion;
            hash = 59 * hash + this.majorVersion;
            hash = 59 * hash + this.bugfixVersion;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final AppVersion other = (AppVersion) obj;
            if (this.minorVersion != other.minorVersion) {
                return false;
            }
            if (this.majorVersion != other.majorVersion) {
                return false;
            }
            if (this.bugfixVersion != other.bugfixVersion) {
                return false;
            }
            return true;
        }

        @Override
        public int compareTo(Object obj) {
            if (obj == null) {
                return -1;
            }
            if (getClass() != obj.getClass()) {
                return -1;
            }
            final AppVersion other = (AppVersion) obj;

            if (this.majorVersion != other.majorVersion) {
                return new Integer(this.majorVersion).compareTo(new Integer(other.majorVersion));
            }
            if (this.minorVersion != other.minorVersion) {
                return new Integer(this.minorVersion).compareTo(new Integer(other.minorVersion));
            }
            if (this.bugfixVersion != other.bugfixVersion) {
                return new Integer(this.bugfixVersion).compareTo(new Integer(other.bugfixVersion));
            }
            return 0;
        }

        public boolean isNewerThan(AppVersion version) {
            return this.compareTo(version) > 0;
        }
    }
    /**
     * Bare-bones set of tags we need to recognise in plugin.xml in order to
     * identify plugins.
     */
    public enum PluginXMLElement {

        PLUGIN,
        ATTRIBUTE,
        PARAMETER,
        PROPERTY,
        IGNORED
    };

    /**
     * Bare-bones set of attributes we need to recognise in plugin.xml in order
     * to identify plugins.
     */
    public enum PluginXMLAttribute {

        ID,
        NAME,
        VALUE,
        VERSION,
        CLASS,
        CATEGORY,
        IGNORED
    };

    public enum Category {

        VISUALIZATION{
            @Override
                    public String toString(){
                return "Visualization";
            }
        },
        ANALYSIS{
            @Override
            public String toString(){
                return "Analysis and Statistics";
            }
        },
        SEARCH{
            @Override
            public String toString(){
                return "Search";
            }
        },
        UTILITY{
            @Override
            public String toString(){
                return "Utility";
            }
        },
        DATASOURCE{
            @Override
            public String toString(){
                return "External Datasource";
            }
        }
    };

    final String className;
    final String version;
    final String name;
    final String sdkVersion;
    final File file;
    final Category category;
    

    public AppDescriptor(String className,  String version, String name, String sdkVersion, String category, File file) {
        this.className = className;

        this.version = version;
        this.name = name;
        this.sdkVersion = sdkVersion;
        this.category = Category.valueOf(category.toUpperCase());
        this.file = file;
    }

    @Override
    public String toString() {
        return name + "-" + version;
    }

    public String getClassName() {
        return className;
    }

    public String getID() {
        return getName();
    }

    public String getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public String getSDKVersion() {
        return sdkVersion;
    }

    public Category getCategory() {
        return category;
    }

    public File getFile() {
        return file;
    }

    @Override
    public int compareTo(AppDescriptor t) {
        return (name + version).compareTo(t.name + t.version);
    }

   
}
