package com.itao.starlite.model;



import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.OrderBy;

/**
 * <p>Represents a Starlite AMO Task. </p>
 * <p>This class has been mapped for Hibernate.</p>
 * @author Celeste Groenewald
 *
 */


@Entity
public class Task implements Cloneable {
	@Id
	@GeneratedValue
	private Integer task_id;

	@Column(unique=true)
	private String task_name;
	
	@Column(nullable=true)
	private String task_desc=null;
	
	
    public Task() {}
	
	
	public void setTaskName(String taskName) {
		this.task_name = taskName;
		System.out.println("SET task_name:"+this.task_name);
	}
	public String getTaskName() {
		return this.task_name;
	}
	public void setTaskDesc(String taskDesc) {
		this.task_desc = taskDesc;
		System.out.println("SET task_desc:"+this.task_desc);
	}
	public String getTaskDesc() {
		return this.task_desc;
	}
	
	public String toString(){
		return "{id:"+task_id+",task_name:"+getTaskName()+",task_desc:"+getTaskDesc()+"}";
	}

	
	public Object clone() throws CloneNotSupportedException {
    	try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			throw(e);
		}
    }
    
}
