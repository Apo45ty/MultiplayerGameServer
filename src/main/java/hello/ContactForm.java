package hello;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
//import org.hibernate.annotations.GenericGenerator;

//@Entity
//@Table(name = "contactform",schema="public")
public class ContactForm {
	
	/*
	 * @Id
	 * 
	 * @Column(name = "formid")
	 * 
	 * @GeneratedValue( strategy= GenerationType.AUTO,
	 * generator="contactform_formid_seq" )
	 * 
	 * @GenericGenerator( name = "native", strategy = "native" )
	 */
	private int formid;
	private String name;
	private String email;
	private String message;
	private String subject;
	private String fromip;
	
	
	public String getFromip() {
		return fromip;
	}
	public void setFromip(String fromip) {
		this.fromip = fromip;
	}
	public int getFormid() {
		return formid;
	}
	public void setFormid(int formid) {
		this.formid = formid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public ContactForm() {
		// TODO Auto-generated constructor stub
	}
	public ContactForm(int formid, String name, String email, String message, String subject, String fromip) {
		super();
		this.formid = formid;
		this.name = name;
		this.email = email;
		this.message = message;
		this.subject = subject;
		this.fromip = fromip;
	}
	@Override
	public String toString() {
		return "ContactForm [formid=" + formid + ", name=" + name + ", email=" + email + ", message=" + message
				+ ", subject=" + subject + ", fromip=" + fromip + "]";
	}
	
	
	
	
}
