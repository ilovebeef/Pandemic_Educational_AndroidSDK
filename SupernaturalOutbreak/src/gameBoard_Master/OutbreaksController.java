package gameBoard_Master;

public class OutbreaksController {

		private Outbreaks model;
		private OutbreaksView view;
		
		public OutbreaksController(Outbreaks model, OutbreaksView view) {
			this.model = model;
	        this.view = view;
	    }
		
		public boolean increaseOutbreaksLevel(){
				// if true, rate incremented by 1
			    // if false, initiate game over
			return model.increaseOutbreaks();
		}
		
		public int getOutbreakLevel() {
			return model.getOutbreakLevel();
		}
		
	    public void updateView() {
	        view.printOutbreaksDetails(model.getOutbreakLevel());
	    }
}
