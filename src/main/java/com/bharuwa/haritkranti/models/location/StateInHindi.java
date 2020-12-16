package com.bharuwa.haritkranti.models.location;

import org.springframework.data.mongodb.core.index.Indexed;
import com.bharuwa.haritkranti.models.BaseObject;
import javax.validation.constraints.NotNull;


/**
 * @author sunaina
 */
public class StateInHindi extends BaseObject {
    public StateInHindi() { }

    public StateInHindi(@NotNull(message = "State name must not be null") String name) {
		super();
		this.name = name;
	}

	@NotNull(message = "State name must not be null")
    @Indexed(unique = true)
    private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
