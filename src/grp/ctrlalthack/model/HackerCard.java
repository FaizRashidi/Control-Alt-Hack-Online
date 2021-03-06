/**
 * Defines Hacker Character Cards
 *   
 * @author Arushad Ahmed
 * @arthor_uri http://arushad.org  
 */

package grp.ctrlalthack.model;

import java.io.Serializable;
import java.util.HashMap;

public class HackerCard implements GameConstants, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8850566825132470566L;
	
	private String name; //name of the character
	private String desc; //character description
	private int kitchen_sink; //kitchen sink skill level
	private HashMap<String,Integer> skills; //skill set
	
	/**
	 * Constructor
	 */
	public HackerCard(String name, String desc, HashMap<String,Integer> skills, int kitchen_sink) {
		this.name = name;
		this.desc = desc;	
		this.setKitchenSink(kitchen_sink);
		this.setSkills(skills);	
	}
	
	/**
	 * @param kitchen_sink the kitchen_sink to set
	 */
	private void setKitchenSink(int kitchen_sink) {
		if ( kitchen_sink < 0 ) {
			throw new IllegalArgumentException("Skill value has to be positive");
		}
		this.kitchen_sink = kitchen_sink;
	}

	/**
	 * @param skills the skills to set
	 */
	private void setSkills(HashMap<String, Integer> skills) {
		if ( skills == null || skills.isEmpty() ) {
			throw new IllegalArgumentException("Skill set cannot be empty");
		} else if ( skills.size() > 7 ) {
			throw new IllegalArgumentException("Skill set can have maximum 7 skills");
		}
		this.skills = skills;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @return the kitchen_sink
	 */
	public int getKitchenSink() {
		return kitchen_sink;
	}
	
	/**
	 * @return the skills
	 */
	public HashMap<String, Integer> getSkills() {
		return skills;
	}
	
	/**
	 * @return the value for a specific skill
	 */
	public int getSkill(String skill) {		
		Integer skill_val = this.getSkills().get(skill);
		if (  skill_val == null ) {
			//return kitchen sink if skill not specified
			return this.getKitchenSink();
		} else {
			return skill_val;
		}
	}
	
	/**
	 * @return the start_cash
	 */
	public int getStartCash() {
		return START_CASH;		
	}

	/**
	 * @return the draw_double
	 */
	public boolean canDrawDouble() {
		return false;
	}	

	/**
	 * @return the fail_penalty
	 */
	public int getHackerCredPenalty() {
		return 0;		
	}
	
	/**
	 * @returns the skill name from slug
	 */
	public static String getSkillName(String skill) {
		String name = "";
		switch (skill) {						
			case HARDWARE:
				name = "Hardware Hacking";
				break;
			case NETWORK:
				name = "Network Ninja";
				break;
			case SOCIAL:
				name = "Social Engineering";
				break;
			case SOFTWARE:
				name = "Software Wizardry";
				break;
			case CRYPT:
				name = "Cryptanalysis";
				break;
			case LOCKPICK:
				name = "Lockpicking";
				break;
			case SEARCH:
				name = "Search Fu";
				break;
			case FORENSICS:
				name = "Forensics";
				break;
			case BARISTA:
				name = "Barista";
				break;
			case WEB:
				name = "Web Procurement";
				break;
			case CONNECTIONS:
				name = "Connections";
				break;
		}
		return name;
	}
	
	/**
	 * Validates a skill slug
	 */
	public static boolean isSkill(String name) {
		return name != null && VALID_SKILLS.contains(name);
	}

}
