package com.nas.recovery.web.action.userBill;

import com.oreon.kg.domain.Employee;
import com.oreon.kg.domain.userBill.UserBill;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import org.apache.commons.lang.StringUtils;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Scope;

import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.Component;
import org.jboss.seam.security.Identity;

import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.annotations.Observer;

import org.witchcraft.base.entity.FileAttachment;

import org.apache.commons.io.FileUtils;
//import org.joda.time.Months;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

@Scope(ScopeType.SESSION)
@Name("userBillAction")
public class UserBillAction extends UserBillActionBase implements
		java.io.Serializable {

	@In(create = true)
	UserBillListQuery userBillList;

	@DataModel(value = "userBillsByEmployee")
	List<UserBill> userBillsByEmployee;

	double totalPayable;
	double totalPaid;

	
	/*****************************Getter and setter **************************
	
	 ***************************************************************************/
	public double getTotalPaid() {
		return totalPaid;
	}

	public void setTotalPaid(double totalPaid) {
		this.totalPaid = totalPaid;
	}

	public double getTotalPayable() {
		return totalPayable;
	}

	public void setTotalPayable(double totalPayable) {
		this.totalPayable = totalPayable;
	}

    /****************************************************************************/

	private List months = new ArrayList<String>();
	private int monthValue;

	private boolean updateTrue;
	private Date dateFrom;
	private Date dateTo;

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	// public UserBillAction() {
	// getMonths().add(new SelectItem(0, "January"));
	// getMonths().add(new SelectItem(1, "February"));
	// getMonths().add(new SelectItem(2, "March"));
	// getMonths().add(new SelectItem(3, "April"));
	// getMonths().add(new SelectItem(4, "May"));
	// getMonths().add(new SelectItem(5, "June"));
	// getMonths().add(new SelectItem(6, "July"));
	// getMonths().add(new SelectItem(7, "August"));
	// getMonths().add(new SelectItem(8, "September"));
	// getMonths().add(new SelectItem(9, "October"));
	// getMonths().add(new SelectItem(10, "November"));
	// getMonths().add(new SelectItem(11, "December"));
	//
	// }

	/**
	 * @return the monthValue
	 */
	public int getMonthValue() {
		return monthValue;
	}

	/**
	 * @param monthValue
	 *            the monthValue to set
	 */
	public void setMonthValue(int monthValue) {
		this.monthValue = monthValue;
	}

	/**
	 * @return the months
	 */
	public List getMonths() {
		return months;
	}

	/**
	 * @param months
	 *            the months to set
	 */
	public void setMonths(List months) {
		this.months = months;
	}

	
	
	
	@SuppressWarnings("static-access")
	public List<UserBill> generatePayment() {
		this.setTotalPayable(0.0);
		String query = "from UserBill bills where bills.minPaymentDueDate between ?1 and ?2 and bills.employee=?3 and bills.amountPaid is NULL order by bills.dateCreated desc ";
		userBillsByEmployee = this.executeQuery(query, dateFrom, dateTo,
				employeeAction.getCurrentLoggedInEmployee());
for (UserBill userBill : userBillsByEmployee) {
	this.setTotalPayable(this.getTotalPayable()+userBill.getMinPayment());
	
}
		return userBillsByEmployee;
	}

	@SuppressWarnings("static-access")
	public List<UserBill> generatePaid() {
this.setTotalPaid(0.0);
		String query = "from UserBill bills where bills.minPaymentDueDate between ?1 and ?2 and bills.employee=?3 and bills.amountPaid is not NULL order by bills.dateCreated desc ";
		userBillsByEmployee = this.executeQuery(query, dateFrom, dateTo,
				employeeAction.getCurrentLoggedInEmployee());
		for (UserBill userBill : userBillsByEmployee) {
			this.setTotalPaid(this.getTotalPaid()+userBill.getAmountPaid());
		}
		return userBillsByEmployee;
	}

	@SuppressWarnings("static-access")
	public List<UserBill> generateAll() {

		String query = "select b from Employee as e , IN (e.userBills) b where e.firstName=?1";
		userBillsByEmployee = this.executeQuery(query,employeeAction.getCurrentLoggedInEmployee().getFirstName());

		return userBillsByEmployee;
	}

	
	/**
	 * @return the updateTrue
	 */
	public boolean isUpdateTrue() {
		return updateTrue;
	}

	/**
	 * @param updateTrue
	 *            the updateTrue to set
	 */
	public void setUpdateTrue(boolean updateTrue) {
		this.updateTrue = updateTrue;
	}

	@Override
	public String save() {
		updateComposedAssociations();

		Employee newEmp = new Employee();
		newEmp = employeeAction.getCurrentLoggedInEmployee();
		this.getUserBill().setEmployee(newEmp);
		return super.save();

	}

	@Override
	public String saveWithoutConversation() {
		updateComposedAssociations();

		Employee newEmp = new Employee();
		newEmp = employeeAction.getCurrentLoggedInEmployee();
		this.getUserBill().setEmployee(newEmp);
		super.save();
		this.generatePayment();
		return "";
	}

}
