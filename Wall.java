public class Wall extends GameObject {	
	public void levelAdvanced(int level) {
		long start = System.currentTimeMillis();
		for (int i = 0; i < getParent().getFieldWidth(); i++) {
			getParent().obstruct(i, 0, this);
			getParent().obstruct(i, getParent().getFieldHeight() - 1, this);		
		}
		for (int i = 0; i < getParent().getFieldHeight(); i++) {	
			getParent().obstruct(0, i, this);
			getParent().obstruct(getParent().getFieldWidth() - 1, i, this);
		}
		System.out.println("\t setup walls " + (System.currentTimeMillis() - start));
		
		int x, y;
		switch (level) {
			case 1: break;
			case 20:
				y = getParent().getFieldHeight() / 2;				
				for (x = 15; x < getParent().getFieldWidth() - 15; x++) {
					getParent().obstruct(x, y, this);
				}
				break;
			case 39:
				y = getParent().getFieldHeight() / 2;				
				for (x = 15; x < getParent().getFieldWidth() - 15; x++) {
					getParent().obstruct(x, y, this);
				}
				x = getParent().getFieldWidth() / 2;				
				for (y = 15; y < getParent().getFieldHeight() - 15; y++) {
					getParent().obstruct(x, y, this);
				}
				break;
			case 40:		
				y = 0;
				x = 0;
				while (x < getParent().getFieldWidth() / 2 - 4 && y < getParent().getFieldHeight() / 2 - 4) {
					getParent().obstruct(x, y, this);
					x++;
					y++;
				}
				x = getParent().getFieldWidth() / 2 + 4;
				y = getParent().getFieldHeight() / 2 - 4;
				while (x < getParent().getFieldWidth() && y < getParent().getFieldHeight()) {
					getParent().obstruct(x, y, this);
					x++;
					y++;
				}
				break;
		}
		System.out.println("\t setup level " + (System.currentTimeMillis() - start));
	}
}
