package in.ac.iitd.cse.iuatc.ews;

import java.io.Serializable;

public class StringPair implements Serializable {

	private static final long serialVersionUID = 1L;
	public final String first;
	public final String second;

	public StringPair(String first, String second) {
		this.first = first;
		this.second = second;
	}
}
