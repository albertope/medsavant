/*
 * Copyright (C) 2014 University of Toronto, Computational Biology Lab.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package org.ut.biolab.medsavant.shared.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author jim
 */
public class LocusComment implements Serializable {

    public static final OntologyType ONTOLOGY_TYPE = OntologyType.HPO;
    private static final boolean DEFAULT_APPROVED_STATUS = false;
    private static final boolean DEFAULT_INCLUDED_STATUS = false;
    private static final boolean DEFAULT_PENDING_REVIEW_STATUS = false;
    private static final boolean DEFAULT_DELETED_STATUS = false;
    
    protected final Integer commentId; 
    private final String user; //Ignored by server
    private final boolean isApproved; //Only used by server if user has permission
    private final boolean isIncluded;//Only used by server if user has permission
    private final boolean isPendingReview;//Only used by server if user has permission
    private final boolean isDeleted;//Only used by server if user has permission, or if user owns comment
    private final Date creationDate;//ignored by server
    private final Date modificationDate; //ignored by server.
    private final String commentText;
    private final OntologyTerm ontologyTerm;

    private LocusComment parentComment;

    
    public LocusComment(LocusComment parentComment, Boolean approveParent, Boolean includeParent, Boolean markParentPending, Boolean markParentDeleted, String comment) {
        //Status change comment tracks the original status of the parent comment.  
        this(parentComment.isApproved(),
                parentComment.isIncluded(),
                parentComment.isPendingReview(),
                parentComment.isDeleted(),
                comment,
                parentComment.getOntologyTerm(),
                new LocusComment(approveParent, includeParent, markParentPending, markParentDeleted, parentComment.getCommentText(), parentComment.getOntologyTerm()));

        //Parent comment contains the new status.
        this.parentComment = new LocusComment(parentComment.getCommentID(), null, approveParent, includeParent, markParentPending, markParentDeleted, null, null, parentComment.getCommentText(), parentComment.getOntologyTerm(), null);        
    }

    public LocusComment(String comment, OntologyTerm ontologyTerm){
        this(DEFAULT_APPROVED_STATUS, DEFAULT_INCLUDED_STATUS, DEFAULT_PENDING_REVIEW_STATUS, DEFAULT_DELETED_STATUS, comment, ontologyTerm);
    }
    
    //
    public LocusComment(Boolean isApproved, Boolean isIncluded, Boolean isPendingReview, Boolean isDeleted, String comment, OntologyTerm ontologyTerm) {
        this(null, null, isApproved, isIncluded, isPendingReview, isDeleted, null, null, comment, ontologyTerm, null);
    }

    public LocusComment(Boolean isApproved, Boolean isIncluded, Boolean isPendingReview, Boolean isDeleted, String comment, OntologyTerm ontologyTerm, LocusComment parentComment) {
        this(null, null, isApproved, isIncluded, isPendingReview, isDeleted, null, null, comment, ontologyTerm, parentComment);
    }

    public LocusComment(Integer commentId, String user, Boolean isApproved, Boolean isIncluded, Boolean isPendingReview, Boolean isDeleted, Date creationDate, Date modificationDate, String commentText, OntologyTerm ontologyTerm, LocusComment parentComment) {
        this.commentId = commentId;
        this.user = user;
        this.isApproved = isApproved;
        this.isIncluded = isIncluded;
        this.isPendingReview = isPendingReview;
        this.isDeleted = isDeleted;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.commentText = commentText;
        this.ontologyTerm = ontologyTerm;
        this.parentComment = parentComment;
    }

    public OntologyTerm getOntologyTerm() {
        return ontologyTerm;
    }

    public String getCommentText() {
        return commentText;
    }

    public String getUser() {
        return user;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public boolean isIncluded() {
        return isIncluded;
    }

    public boolean isPendingReview() {
        return isPendingReview;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getModificationDate() {
        return this.modificationDate;
    }

    public boolean statusChanged() {
        return parentComment != null
                && !(parentComment.isApproved() == isApproved()
                && parentComment.isDeleted() == isDeleted()
                && parentComment.isIncluded() == isIncluded()
                && parentComment.isPendingReview() == isPendingReview());
    }

    public LocusComment getOriginalComment() {
        return parentComment;
    }

    public Integer getCommentID() {
        return commentId;
    }
}
