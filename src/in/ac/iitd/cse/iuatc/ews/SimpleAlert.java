package in.ac.iitd.cse.iuatc.ews;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import oasis.names.tc.emergency.cap._1.Alert;

public class SimpleAlert implements Serializable {
	private static final long serialVersionUID = 1L;
	private String alertId;
	private String eventId; // TODO
	private Date issueDate;
	private Date receiveDate;
	private Date expiryDate; // TODO
	private String eventCategory;
	private String eventSubCategory;
	private boolean cancelled; // TODO
	private String severity;
	private String certainty;
	private String url;
	private String headline;
	private String description;
	private String instruction;
	private String areaDesc;
	private Alert cap;
	private double lat;
	private double lng;
	private double radiusInMeters;
	private boolean mapDataRecieved;

	public void setMapDataRecieved(boolean mapDataRecieved) {
		this.mapDataRecieved = mapDataRecieved;
	}

	Map<AlertInfoHeadings, StringPair> infoMap;

	public SimpleAlert() {
		this.infoMap = new TreeMap<AlertInfoHeadings, StringPair>();
	}

	public SimpleAlert(Alert cap) {
		this.infoMap = new TreeMap<AlertInfoHeadings, StringPair>();
		this.setAlertId(cap.getIdentifier());
		this.setHeadline(cap.getInfo().get(0).getHeadline());
		this.setDescription(cap.getInfo().get(0).getDescription());
		this.setInstruction(cap.getInfo().get(0).getInstruction());
		this.setAreaDesc(cap.getInfo().get(0).getArea().get(0)
				.getAreaDesc());
		this.setUrl(cap.getInfo().get(0).getWeb());
		this.setIssueDate(cap.getSent());
		if (cap.getInfo().get(0).getArea().get(0).getCircle()
				.size() > 0) {
			String circle = cap.getInfo().get(0).getArea().get(0)
					.getCircle().get(0);
			String[] separated = circle.split(" ");
			this.setRadiusInMeters(Double.parseDouble(separated[1])*1000);  /*CAP has radius in km*/
			separated = separated[0].split(",");
			this.setLat(Double.parseDouble(separated[0]));
			this.setLng(Double.parseDouble(separated[1]));
			this.setMapDataRecieved(true);
		} else {
			this.setMapDataRecieved(false);
		}

		this.setCap(cap);
	}

	public boolean isMapDataRecieved() {
		return this.mapDataRecieved;
	}

	public String getAlertId() {
		return this.alertId;
	}

	public String getAreaDesc() {
		return this.areaDesc;
	}

	public Alert getCap() {
		return this.cap;
	}

	public String getCertainty() {
		return this.certainty;
	}

	public String getDescription() {
		return this.description;
	}

	public String getEventCategory() {
		return this.eventCategory;
	}

	public String getEventId() {
		return this.eventId;
	}

	public String getEventSubCategory() {
		return this.eventSubCategory;
	}

	public Date getExpiryDate() {
		return this.expiryDate;
	}

	public String getHeadline() {
		return this.headline;
	}

	public String getInstruction() {
		return this.instruction;
	}

	public Date getIssueDate() {
		return this.issueDate;
	}

	public double getLat() {
		return this.lat;
	}

	public double getLng() {
		return this.lng;
	}

	public double getRadiusInMeters() {
		return this.radiusInMeters;
	}

	public Date getReceiveDate() {
		return this.receiveDate;
	}

	public String getSeverity() {
		return this.severity;
	}

	public String getUrl() {
		return this.url;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	private void setAlertId(String alertId) {
		this.alertId = alertId;
	}

	private void setAreaDesc(String areaDesc) {
		this.areaDesc = areaDesc;
		this.infoMap.put(AlertInfoHeadings.AFFECTED_AREAS, new StringPair(
				AlertInfoHeadings.AFFECTED_AREAS.toString(), areaDesc));
	}

	private void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	private void setCap(Alert cap) {
		this.cap = cap;
	}

	private void setCertainty(String certainty) {
		this.certainty = certainty;
	}

	private void setDescription(String description) {
		this.description = description;
		this.infoMap.put(AlertInfoHeadings.ALERTDETAILS, new StringPair(
				AlertInfoHeadings.ALERTDETAILS.toString(), description));
	}

	private void setEventCategory(String eventCategory) {
		this.eventCategory = eventCategory;
	}

	private void setEventId(String eventId) {
		this.eventId = eventId;
	}

	private void setEventSubCategory(String eventSubCategory) {
		this.eventSubCategory = eventSubCategory;
	}

	private void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	private void setHeadline(String headline) {
		this.headline = headline;
	}

	private void setInstruction(String instruction) {
		this.instruction = instruction;
		this.infoMap.put(AlertInfoHeadings.INSTRUCTION, new StringPair(
				AlertInfoHeadings.INSTRUCTION.toString(), instruction));
	}

	private void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
		this.infoMap.put(AlertInfoHeadings.ISSUEDATE, new StringPair(
				AlertInfoHeadings.ISSUEDATE.toString(), new SimpleDateFormat(
						"dd MMM hh:mm a", Locale.ENGLISH).format(issueDate)));
	}

	private void setLat(double lat) {
		this.lat = lat;
	}

	private void setLng(double lng) {
		this.lng = lng;
	}

	private void setRadiusInMeters(double radius) {
		this.radiusInMeters = radius;
	}

	private void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	private void setSeverity(String severity) {
		this.severity = severity;
	}

	private void setUrl(String url) {
		this.url = url;
		this.infoMap.put(AlertInfoHeadings.WEB_URL, new StringPair(
				AlertInfoHeadings.WEB_URL.toString(), url));

	}

	@Override
	public String toString() {
		return this.getHeadline();
	}
}
