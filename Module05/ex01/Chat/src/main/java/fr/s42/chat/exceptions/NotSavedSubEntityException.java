package fr.s42.chat.exceptions;

public class NotSavedSubEntityException extends RuntimeException {
	public NotSavedSubEntityException(String m){
		super(m);
	}
}
