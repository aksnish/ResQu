/**********************************
* @(#)				SummaryDocument.java
* @author			Delroy Cameron
* @version			1.0 Nov 26, 2014
*/
package gov.nih.nlm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class SummaryDocument implements Serializable {

	private String topic;
	private int id;
	private String type;
	private List<String> contentList = new ArrayList<String>();
	
	public SummaryDocument(){}
	
	public SummaryDocument(String topic, String type){
		this.topic = topic;
		this.type = type;
	}
	
	public SummaryDocument(String topic, String type, int id){
		this.topic = topic;
		this.type = type;
		this.id = id;
	}
	
	public String getTopic() {
		return topic;
	}
	public int getId() {
		return id;
	}
	public String getType() {
		return type;
	}
	public List<String> getContentList() {
		return contentList;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public void setContentList(List<String> contentList) {
//		for(String keyPhrase : contentList){
//			this.contentList.add(keyPhrase);
//		}
		this.contentList = new ArrayList<String>(contentList);
	}
	public int hashCode (){
		return this.id*topic.hashCode()*type.hashCode();
	}
}
