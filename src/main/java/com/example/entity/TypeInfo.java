package com.example.entity;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "type_info")
public class TypeInfo  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name = "name")
	private String name;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TypeInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
