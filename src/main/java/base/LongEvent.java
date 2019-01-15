package base;
/**
 * 定义一个事件
 * @author whl
 *
 */
public class LongEvent {

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private long value;
	private String name="";

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}
	
}
