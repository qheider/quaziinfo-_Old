package com.nas.recovery.web.action.workflowmgt;

import org.jboss.seam.annotations.bpm.CreateProcess;
import org.jboss.seam.annotations.bpm.EndTask;
import org.jboss.seam.annotations.bpm.StartTask;
import org.witchcraft.jbpm.BaseJbpmProcessAction;

public class ReoWorkflowProcessActionBase extends BaseJbpmProcessAction
		implements
			java.io.Serializable {

//	protected com.nas.recovery.domain.loan.LoanData token = new com.nas.recovery.domain.loan.LoanData();

	@CreateProcess(definition = "reoWorkflow")
	public void startProcess() {

	}

	@StartTask
	public void startConflictCheck() {

	}

	@EndTask(transition = "conflict")
	public void conflictConflictCheck() {

	}
	@EndTask(transition = "accept")
	public void acceptConflictCheck() {

	}

	@StartTask
	public void startPmAcceptFile() {

	}

	@EndTask(transition = "reject")
	public void rejectPmAcceptFile() {

	}
	@EndTask(transition = "accept")
	public void acceptPmAcceptFile() {

	}

	@StartTask
	public void startReAcceptFile() {

	}

	@EndTask(transition = "accept")
	public void acceptReAcceptFile() {

	}
	@EndTask(transition = "reject")
	public void rejectReAcceptFile() {

	}

	@StartTask
	public void startReassignLawyer() {

	}

	@EndTask(transition = "legalReassign")
	public void legalReassignReassignLawyer() {

	}

	@StartTask
	public void startVerifyAllocations() {

	}

	@EndTask(transition = "complete")
	public void completeVerifyAllocations() {

	}

	@StartTask
	public void startOrderAppraisal() {

	}

	@EndTask(transition = "complete")
	public void completeOrderAppraisal() {

	}

	@StartTask
	public void startAppraisalReceived() {

	}

	@EndTask(transition = "complete")
	public void completeAppraisalReceived() {

	}

	@StartTask
	public void startReviewValuation() {

	}

	@EndTask(transition = "reviewed")
	public void reviewedReviewValuation() {

	}

	@StartTask
	public void startOrderFullCMA() {

	}

	@EndTask(transition = "complete")
	public void completeOrderFullCMA() {

	}

	@StartTask
	public void startWaitState1() {

	}

	@EndTask(transition = "gotoReviewValuation")
	public void gotoReviewValuationWaitState1() {

	}

	@StartTask
	public void startPropertyCheckAndCreteQuote() {

	}

	@EndTask(transition = "complete")
	public void completePropertyCheckAndCreteQuote() {

	}

	@StartTask
	public void startReviewQuoteForApproval() {

	}

	@EndTask(transition = "complete")
	public void completeReviewQuoteForApproval() {

	}

	@StartTask
	public void startReviewApprovedQuote() {

	}

	@EndTask(transition = "reviewed")
	public void reviewedReviewApprovedQuote() {

	}

	@StartTask
	public void startQuoteWorkComplete() {

	}

	@EndTask(transition = "complete")
	public void completeQuoteWorkComplete() {

	}

	@StartTask
	public void startReviewQuoteworkComplete() {

	}

	@EndTask(transition = "complete")
	public void completeReviewQuoteworkComplete() {

	}

	@StartTask
	public void startListProperty() {

	}

	@EndTask(transition = "listed")
	public void listedListProperty() {

	}

	@StartTask
	public void startEnterOfferDetails() {

	}

	@EndTask(transition = "noOffer")
	public void noOfferEnterOfferDetails() {

	}
	@EndTask(transition = "offerEntered")
	public void offerEnteredEnterOfferDetails() {

	}

	@StartTask
	public void startCheckConditionsWaived() {

	}

	@EndTask(transition = "notWaived")
	public void notWaivedCheckConditionsWaived() {

	}
	@EndTask(transition = "sold")
	public void soldCheckConditionsWaived() {

	}

	@StartTask
	public void start30DaysReport() {

	}

	@EndTask(transition = "uploaded")
	public void uploaded30DaysReport() {

	}
	@EndTask(transition = "notRequired")
	public void notRequired30DaysReport() {

	}

	@StartTask
	public void startReview30DaysReport() {

	}

	@EndTask(transition = "reviewed")
	public void reviewedReview30DaysReport() {

	}

	@StartTask
	public void startReviewSaleOfProperty() {

	}

	@EndTask(transition = "reviewed")
	public void reviewedReviewSaleOfProperty() {

	}

	@StartTask
	public void startSaleReviewed() {

	}

	@EndTask(transition = "complete")
	public void completeSaleReviewed() {

	}

	@StartTask
	public void startUploadClosingDocuments() {

	}

	@EndTask(transition = "uploaded")
	public void uploadedUploadClosingDocuments() {

	}

	@StartTask
	public void startExecuteClosingDocuments() {

	}

	@EndTask(transition = "complete")
	public void completeExecuteClosingDocuments() {

	}

	@StartTask
	public void startTrustLedger() {

	}

	@EndTask(transition = "uploaded")
	public void uploadedTrustLedger() {

	}

	@StartTask
	public void startSaleProceedDeposited() {

	}

	@EndTask(transition = "deposited")
	public void depositedSaleProceedDeposited() {

	}
	@EndTask(transition = "extension")
	public void extensionSaleProceedDeposited() {

	}

	@StartTask
	public void startUploadFinalInvoices() {

	}

	@EndTask(transition = "uploaded")
	public void uploadedUploadFinalInvoices() {

	}

	@StartTask
	public void startKeysDelivered() {

	}

	@EndTask(transition = "complete")
	public void completeKeysDelivered() {

	}

	@StartTask
	public void startUploadInvoices() {

	}

	@EndTask(transition = "uploaded")
	public void uploadedUploadInvoices() {

	}

	@StartTask
	public void startCloseFile() {

	}

	@EndTask(transition = "fileClose")
	public void fileCloseCloseFile() {

	}

	@StartTask
	public void startClaimDecision() {

	}

	@EndTask(transition = "notRequired")
	public void notRequiredClaimDecision() {

	}
	@EndTask(transition = "proceed")
	public void proceedClaimDecision() {

	}

	@StartTask
	public void startFileMainInsuranceClaim() {

	}

	@EndTask(transition = "filed")
	public void filedFileMainInsuranceClaim() {

	}

	@StartTask
	public void startMainInsuranceClaimPaid() {

	}

	@EndTask(transition = "claimPaid")
	public void claimPaidMainInsuranceClaimPaid() {

	}

	@StartTask
	public void startReassignPM() {

	}

	@EndTask(transition = "pmReassign")
	public void pmReassignReassignPM() {

	}

	@StartTask
	public void startReassignRealtor() {

	}

	@EndTask(transition = "reReassign")
	public void reReassignReassignRealtor() {

	}

}
