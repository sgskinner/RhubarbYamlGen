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
public class YamlLineConstants {

}
