package testPackage;

public class MarketData implements Comparable<MarketData>{
	public String name;
	public String symbol;
	public String isin;
	public String lastRate;
	public String change;
	public double turnOver;
	public String lastTrade;
	public String basePrice;
	public String openingPrice;
	public String linkCompany;
	public String href;
	
	//constructor
	public MarketData(String name, String symbol, String isin, String lastRate, String change, double turnOver,
			String lastTrade, String basePrice, String openingPrice, String href)  {
		this.name = name;
		this.symbol = symbol;
		this.isin = isin;
		this.lastRate = lastRate;
		this.change = change;
		this.turnOver = turnOver;
		this.lastTrade = lastTrade;
		this.basePrice = basePrice;
		this.href=href;
	}

	@Override
	public String toString() {
		return "Name=" + name + ", Symbol=" + symbol + ", ISIN=" + isin + ", Last-Rate=" + lastRate
				+ ", Change=" + change + ", Last-Trade=" + lastTrade + ", Base-Price="
				+ basePrice + ", Opening-Price=" + openingPrice+", \nTurnover=" + turnOver+"\n";
	}

	@Override
	public int compareTo(MarketData o) {
		if(turnOver> o.turnOver)
			return -1;
		else
			return 1;
		
	}
	
	
	
	

	
	

}
