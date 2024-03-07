
package acme.roles;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractRole;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Auditor extends AbstractRole {
	//Serialization indentifier --------------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	//Atributes ------------------------------------------------------------------------

	@NotBlank
	@Length(max = 75)
	private String				firm;

	@NotBlank
	@Length(max = 25)
	private String				professionalId;

	@NotBlank
	@Length(max = 100)
	private String				certifications;

	@URL
	private String				link;
}
