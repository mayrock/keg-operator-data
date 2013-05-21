package edu.thu.keg.mdap.provider;

public interface ISsqlQuery {
	public String SelectFromWhere(String selectwhat, String fromwhat,
			String wherewhat);

	public String SelectFrom(String selectwhat, String fromwhat);

}
