package com.vogella.prioritizer.eclipseplugin.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

import com.vogella.spring.data.entities.RankedBug;

public class RankedBugViewerComparator extends ViewerComparator {

	private int propertyIndex;
	private static final int DESCENDING = 1;
	private int direction = DESCENDING;

	public RankedBugViewerComparator() {
		this.propertyIndex = 0;
		direction = DESCENDING;
	}

	public int getDirection() {
		return direction == 1 ? SWT.DOWN : SWT.UP;
	}

	public void setColumn(int column) {
		if (column == this.propertyIndex) {
			// Same column as last sort; toggle the direction
			direction = 1 - direction;
		} else {
			// New column; do an ascending sort
			this.propertyIndex = column;
			direction = DESCENDING;
		}
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		RankedBug p1 = (RankedBug) e1;
		RankedBug p2 = (RankedBug) e2;
		int rc = 0;
		switch (propertyIndex) {
		case 0:
			if (p1.getBugIdBugzilla() == p2.getBugIdBugzilla()) {
				rc = 0;
			} else {
				rc = p1.getBugIdBugzilla() >= p2.getBugIdBugzilla() ? 1 : -1;
			}
			break;
		case 1:
			rc = compareTimestamps(p1, p2);
			break;
		case 2:
			rc = compareTimestamps(p1, p2);
			break;
		case 3:
			rc = p1.getComponent().compareTo(p2.getComponent());
			break;
		case 4:
			rc = p1.getReporter().compareTo(p2.getReporter());
			break;
		case 5:
			rc = p1.getAssignedTo().compareTo(p2.getAssignedTo());
			break;
		case 6:
			rc = p1.getStatus().compareTo(p2.getStatus());
			break;
		case 7:
			if (p1.getVotes() == p2.getVotes()) {
				rc = 0;
			} else {
				rc = p1.getVotes() >= p2.getVotes() ? 1 : -1;
			}
			break;
		case 13:
			rc = p1.getTitle().compareTo(p2.getTitle());
			break;
		default:
			rc = 0;
		}
		// If descending order, flip the direction
		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}

	private long convertTimestamp(String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(sdf.parse(dateString));
			return calendar.getTimeInMillis();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0l;
	}

	private int compareTimestamps(RankedBug rankedBug1, RankedBug rankedBug2) {
		long p1Timestamp = convertTimestamp(rankedBug1.getCreated());
		long p2Timestamp = convertTimestamp(rankedBug2.getCreated());
		if (p1Timestamp == p2Timestamp) {
			return 0;
		} else {
			return p1Timestamp >= p2Timestamp ? 1 : -1;
		}
	}
}
