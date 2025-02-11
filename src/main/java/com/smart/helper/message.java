package com.smart.helper;

public class message {
private String content;
private String type;

public message() {
	super();
	// TODO Auto-generated constructor stub
}
public message(String content, String type) {
	super();
	this.content = content;
	this.type = type;
}
public String getContent() {
	return content;
}
public void setContent(String content) {
	this.content = content;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
@Override
public String toString() {
	return "message [content=" + content + ", type=" + type + "]";
}

}
