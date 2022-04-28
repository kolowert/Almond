package fun.kolowert.hplayer;

public enum GameType {
	
	SUPER {
		private static final int COMBSET_SIZE = 6;
		private static final int PLAYSET_SIZE = 6;
		private static final int GAMESET_SIZE = 52;
		public int getCombSetSize() {
			return COMBSET_SIZE;
		}
		public int getPlaySetSize() {
			return PLAYSET_SIZE;
		}
		public int getGameSetSize() {
			return GAMESET_SIZE;
		}
		public String histFile() {
			return "super.txt";
		}
	},
	
	MAXI {
		private static final int COMBSET_SIZE = 5;
		private static final int PLAYSET_SIZE = 5;
		private static final int GAMESET_SIZE = 45;
		public int getCombSetSize() {
			return COMBSET_SIZE;
		}
		public int getPlaySetSize() {
			return PLAYSET_SIZE;
		}
		public int getGameSetSize() {
			return GAMESET_SIZE;
		}
		public String histFile() {
			return "maxi.txt";
		}
	},
	
	KENO {
		private static final int COMBSET_SIZE = 6;
		private static final int PLAYSET_SIZE = 20;
		private static final int GAMESET_SIZE = 80;
		public int getCombSetSize() {
			return COMBSET_SIZE;
		}
		public int getPlaySetSize() {
			return PLAYSET_SIZE;
		}
		public int getGameSetSize() {
			return GAMESET_SIZE;
		}
		public String histFile() {
			return "keno.txt";
		}
	};
	
	public abstract int getCombSetSize();
	public abstract int getPlaySetSize();
	public abstract int getGameSetSize();
	public abstract String histFile();
}
