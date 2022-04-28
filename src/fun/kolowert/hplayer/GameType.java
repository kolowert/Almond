package fun.kolowert.hplayer;

public enum GameType {
	
	SUPER {
		public String histFile() {
			return "super.txt";
		}
	},
	
	MAXI {
		public String histFile() {
			return "maxi.txt";
		}
	},
	
	KENO {
		public String histFile() {
			return "keno.txt";
		}
	};

	public abstract String histFile();
}
