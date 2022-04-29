package fun.kolowert.hplayer;

public enum GameType {
	
	MAXI(5, 5, 45, "maxi.txt"),
	SUPER(6, 6, 52, "super.txt"),
	KENO(10, 10, 80, "keno.txt");
	
	private final int combSetSize;
	private final int playSetSize;
	private final int gameSetSize;
	private final String histFileName;
	
	GameType(int combSize, int playSize, int gameSize, String fileName) {
		combSetSize = combSize;
		playSetSize = playSize;
		gameSetSize = gameSize;
		histFileName = fileName;
	}

	public int getCombSetSize() {
		return combSetSize;
	}

	public int getPlaySetSize() {
		return playSetSize;
	}

	public int getGameSetSize() {
		return gameSetSize;
	}
	
	public String getHistFileName() {
		return histFileName;
	}
}
