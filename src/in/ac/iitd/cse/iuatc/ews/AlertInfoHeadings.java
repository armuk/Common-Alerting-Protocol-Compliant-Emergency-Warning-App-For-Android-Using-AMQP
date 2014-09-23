package in.ac.iitd.cse.iuatc.ews;

public enum AlertInfoHeadings {
	ISSUEDATE {
		@Override
		public String toString() {
			return "Date of issue";
		}
	},
	ALERTDETAILS {
		@Override
		public String toString() {
			return "Alert Details";
		}

	},
	AFFECTED_AREAS {
		@Override
		public String toString() {
			return "Areas to be affected";
		}
	},
	INSTRUCTION {
		@Override
		public String toString() {
			return "Instruction issued for public";
		}
	},
	WEB_URL {
		@Override
		public String toString() {
			return "Web link for further details";
		}
	};

}
