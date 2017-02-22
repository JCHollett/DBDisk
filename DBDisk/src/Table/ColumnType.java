package Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public enum ColumnType implements Comparable<ColumnType> {
	STRING("VARCHAR"), DOUBLE("DOUBLE"), FLOAT("FLOAT"), DECIMAL("DECIMAL"), SERIAL("SERIAL"), INTEGER("INTEGER"), INT("INT"), TINYINT("TINYINT"), SMALLINT("SMALLINT"), MEDIUMINT(
	"MEDIUMINT"), BIGINT("BIGINT"), DATETIME("DATETIME"), DATE("DATE"), PRIMARY_KEY("PRIMARY KEY");
	public enum Parameter implements Comparable<Parameter> {
		UNSIGNED("UNSIGNED"), SIGNED("SIGNED"), NOT_NULL("NOT NULL"), AUTO_INC("AUTO_INCREMENT"), ZERO_FILL("ZEROFILL");
		private String data;
		
		Parameter(String x) {
			this.data = x;
		}
		
		@Override
		public String toString() {
			return this.data;
		}
	}
	
	private String original = "";
	private String dataType = "";
	private String size = "";
	private List<Parameter> suffix;
	
	ColumnType(String x) {
		this.original = x;
		this.dataType = x;
	}
	
	public ColumnType setParameter(int x) {
		this.size = new String("(" + x + ")");
		this.dataType = new String(this.original + this.size);
		return this;
	}
	
	public ColumnType setParameter(int x, int y) {
		this.size = new String("(" + x + "," + y + ")");
		this.dataType = new String(this.original + this.size);
		return this;
	}
	
	public ColumnType setParameter(String var) {
		this.size = new String(" ( " + var + " ) ");
		this.dataType = new String(this.original + this.size);
		return this;
	}
	
	public ColumnType setSuffix(Parameter ... x) {
		this.suffix = new ArrayList<Parameter>();
		Arrays.sort(x);
		for (Parameter p : x) {
			if (!this.suffix.contains(x)) {
				this.suffix.add(p);
			}
		}
		return this;
	}
	
	@Override
	public String toString() {
		String x = this.dataType;
		if (this.suffix != null) {
			for (Parameter p : this.suffix) {
				x += " " + p;
			}
		}
		this.dataType = this.original;
		return x;
	};

}
