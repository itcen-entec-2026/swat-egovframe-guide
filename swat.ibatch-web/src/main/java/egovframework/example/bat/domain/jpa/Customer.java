package egovframework.example.bat.domain.jpa;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author 배치실행개발팀
 * @since 2012. 07.25
 * @version 1.0
 * @see
 *  <pre>
 *      개정이력(Modification Information)
 *
 *  수정일         수정자       수정내용
 *  ----------   --------   ---------------------------
 *  2023.03.31   신용호       최초 생성
 *  </pre>
 */

@Entity
@Table(name="customer")
public class Customer {
	
	@Id
	//@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	private long version;
	private String name;
	private BigDecimal credit;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getVersion() {
		return version;
	}
	public void setVersion(long version) {
		this.version = version;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getCredit() {
		return credit;
	}
	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

}