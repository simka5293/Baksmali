package com.mycompany.utils;

public class BadStatusException extends Exception 
{
	private static final long serialVersionUID = 1L;
	private int status;
	public BadStatusException(int status)
	{
		this.status = status;
	}
	public int getStatus()
	{
		return status;
	}
}
