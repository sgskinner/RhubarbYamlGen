package org.sgs.rhubarb.yaml;


/*
 *    A file to hold format string for the various types of lines found in YAML
 * configuration files. The intent is to for the YamlDriver to use String.format
 * to fill in any  placeholders of the constant, and then assemple each of the
 * lines into a well-formatted and complete YAML configurations for each of the
 * job streams(2 per job, one for success, 2nd for failure). This strategy
 * should ensure that formatting of a completed output file is uniform  across
 * all configurations. Also, this centralizes all formatting to one intersection
 * for any desired modification across the board.
 */
public class YamlFormatConstants {

	
	/**
	 * Purpose: Required at beginning of file.
	 * 
	 * Ordinal: 0
	 * Required: true
	 * Leading Spaces: 0
	 * Num args: 0
	 * Args: N/A
	 * Example: N/A
	 */
	public final static YamlFormatsTemplate FIRST_ENTRY_TEMPLATE = new YamlFormatsTemplate("---%n");
	
	
	/**
	 * Purpose: Short name for job stream.
	 * 
	 * Ordinal: 1
	 * Required: true
	 * Leading Spaces: 2
	 * Num args: 1
	 * Args: <name>
	 * Example: String.format(JOB_NAME_LINE, "ARCHIBUS");
	 */
	public final static YamlFormatsTemplate JOB_NAME_ENTRY_TEMPLATE = new YamlFormatsTemplate("name: %s%n");
	
	
	/**
	 * Purpose: Signifies beginning of "target" entries
	 * 
	 * Ordinal: 2
	 * Required: true
	 * Leading Spaces: 0
	 * Num args: 0
	 * Args: N/A
	 * Example: N/A
	 */
	public final static YamlFormatsTemplate OUTPUT_ENTRY_TEMPLATE = new YamlFormatsTemplate("output:%n");
	

	/**
	 * Purpose: Hash of targets
	 * 
	 * Ordinal: 3
	 * Required: true
	 * Leading Spaces: 2
	 * Num args: 1
	 * Args: <name>
	 * Example: String.format(TARGET_NAME_ENTRY_TEMPLATE.getRootFormat(), "job_ok");
	 */
	public final static YamlFormatsTemplate TARGET_NAME_ENTRY_TEMPLATE = new YamlFormatsTemplate("  %s:%n");
	
	
	/* ************************************************************************
	 * Reset ordinal here; the only remaining entry types are
	 * children of TargetEntry, and the oridnal is relative
	 * to the siblings underneath the parent TargetEntry 
	 *************************************************************************/
	
	
	/**
	 * Purpose: This is the subject line of the email to be sent on behalf of the job.
	 * 
	 * Ordinal: 0
	 * Required: true
	 * Leading Spaces: 2
	 * Num args: 3
	 * Args: <ENV> <JOB_NAME> <DESCRIPTION>
	 * Example: String.format(SUBJECT_LINE, "PRD", "EINVEXTR", "eInvoice Processing has completed.");
	 */
	public final static YamlFormatsTemplate SUBJECT_ENTRY_TEMPLATE = new YamlFormatsTemplate("  subject: %s - %s - %s%n");
	
	
	/**
	 * Purpose: This is the message of the email to be sent on behalf of the job.
	 * 
	 * Ordinal: 1
	 * Required: true
	 * Leading Spaces: 2 leading
	 * Num args: 1
	 * Args: <MESSAGE>
	 * Example: String.format(MESSAGE_LINE, "    This is a%n    multi-line message%n    spanning three lines.");
	 */
	public final static YamlFormatsTemplate MESSAGE_ENTRY_TEMPLATE = new YamlFormatsTemplate("  message: >%n", "    %s%n");
	
	/**
	 * Purpose: Line starter for list of "To:" recipients of email. The
	 *          formating of the list is provided by YamlFormatter.java
	 * 
	 * Ordinal: 2
	 * Required: true
	 * Leading Spaces: 2 leading
	 * Num args: 1 to many
	 * Args: <email1, email2, ..., lastEmail>
	 * Example: 
	 */
	public final static YamlFormatsTemplate TO_RECIPIENTS_ENTRY_TEMPLATE = new YamlFormatsTemplate("  to:%n", "  - %s%n");
	
	
	/**
	 * Purpose: The "cc" recipient line starter.
	 * 
	 * Ordinal: 3
	 * Required: false
	 * Leading Spaces: 2 leading
	 * Num args: 1 to many
	 * Args: <email_1, email_2, ..., email_n>
	 * Example: 
	 */
	public final static YamlFormatsTemplate CC_RECIPIENT_ENTRY_TEMPLATE = new YamlFormatsTemplate("  cc:%n", "  - %s%n");
	
	
	/**
	 * Purpose: The directory in which to pattern match files for attachment to
	 *          the email.
	 * 
	 * Ordinal: 4
	 * Required: false
	 * Leading Spaces: 2 leading
	 * Num args: 1
	 * Args: <dirPath>
	 * Example: String.format(ATTACHMENTS_DIR_LINE, "$CONFIG_HOME/some/config/dir"); 
	 */
	public static final YamlFormatsTemplate ATTACHMENTS_DIR_ENTRY_TEMPLATE = new YamlFormatsTemplate("  attachments_dir: %s%n", "  - %s%n");
	
	
	/**
	 * Purpose: The directory in which to pattern match files for attachment to
	 *          the email.
	 * 
	 * Ordinal: 5
	 * Required: false
	 * Leading Spaces: 2 leading
	 * Num args: 1 to Many
	 * Args: <pattern_1, pattern_2, ..., pattern_n>
	 * Example: 
	 */
	public static final YamlFormatsTemplate ATTACHMENTS_GLOBS_ENTRY_TEMPLATE = new YamlFormatsTemplate("  attachments_globs:%n", "  - %s%n");

	
}
