package gov.nih.nlm.semrep.utils;

public class Predication {

	String subject;
	String predicate;
	String object;

	public Predication() {}

	public String getSubject(String line) {
		return line.split("\\|")[3].trim();
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getPredicate(String line) {
		return line.split("\\|")[8].trim();
	}
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}
	public String getObject(String line) {
		return line.split("\\|")[10].trim();
	}
	public void setObject(String object) {
		this.object = object;
	}

	public String getpredication(String subject, String predicate, String object){
		String delimiter = "-";
		return subject+delimiter+predicate+delimiter+object;
	}
}
