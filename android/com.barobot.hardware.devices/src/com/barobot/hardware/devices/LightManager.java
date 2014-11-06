package com.barobot.hardware.devices;

import com.barobot.common.Initiator;
import com.barobot.common.constant.Constant;
import com.barobot.hardware.devices.i2c.Carret;
import com.barobot.hardware.devices.i2c.Upanel;
import com.barobot.parser.Queue;
import com.barobot.parser.message.AsyncMessage;
import com.barobot.parser.message.Mainboard;

public class LightManager {
	String[] defaultDemo = {"","",""};
	private BarobotConnector barobot;
	public static int[] top_index 		= {10,23,11,22,12,21,13,20,14,19,15,18};			// numery butelek na numery ledow top
	public static int[] bottom_index	= {-1,24,-1,25,-1,26,-1,27,-1,28,-1,29};			// numery butelek na numery ledow bottom
	public static int[] bottom_leds		= {24,25,26,27,28,29};

	public LightManager(BarobotConnector barobot2) {
		this.barobot	= barobot2;
	}

	public void startDemo() {
		Queue q = barobot.main_queue;
		LightManager lm =barobot.lightManager;
	//	lm.tecza( barobot, q, 10 );				// ok
	//	lm.loading(barobot, q, 10);				// ok
	//	lm.mrygajRGB( barobot, q, 10 , 400);	// ok
	//	lm.linijka( barobot, q, 10, 700 );		// nudne
		lm.flaga( q, 10, 700 );
		lm.nazmiane( q, 10, 700 );
	//	lm.strobo( barobot, q, 60 );			// ok
	//	lm.zapal(barobot, q);					// ok
	}

	public void loading(final Queue q, final int repeat) {
		final int time =200;
		q.add( new AsyncMessage( true ){		// na koncu zamknij
			@Override
			public String getName() {
				return "tecza";
			}
			@Override
			public Queue run(Mainboard dev, Queue queue) {
				Queue q2 = new Queue();
				turnOffLeds( q2 );
				Initiator.logger.i( "LightsManager", "start2 loading");
				
				for (int i=0;i<repeat;i+=1){
					//barobot.driver_x.d.moveTo( q, 1000);
					//barobot.setLedsByBottle(q, 1, "01", 255, 255, 0, 0, true);
					//barobot.color_by_bottle(q2, 1, true, 255, 255, 0);
					color_by_bottle(q2, 1, true, 200, 0, 0);	q2.addWait( time );color_by_bottle(q2, 1, true, 10, 0, 0);
					color_by_bottle(q2, 3, true, 200, 0, 0);	q2.addWait( time );color_by_bottle(q2, 3, true, 10, 0, 0);
					color_by_bottle(q2, 5, true, 200, 0, 0);	q2.addWait( time );color_by_bottle(q2, 5, true, 10, 0, 0);
					color_by_bottle(q2, 7, true, 200, 0, 0);	q2.addWait( time );color_by_bottle(q2, 7, true, 10, 0, 0);
					color_by_bottle(q2, 9, true, 200, 0, 0);	q2.addWait( time );color_by_bottle(q2, 9, true, 10, 0, 0);
					color_by_bottle(q2, 11, true, 200, 0, 0);	q2.addWait( time );color_by_bottle(q2, 11, true, 10, 0, 0);
					color_by_bottle(q2, 9, true, 200, 0, 0);	q2.addWait( time );color_by_bottle(q2, 9, true, 10, 0, 0);
					color_by_bottle(q2, 7, true, 200, 0, 0);	q2.addWait( time );color_by_bottle(q2, 7, true, 10, 0, 0);
					color_by_bottle(q2, 5, true, 200, 0, 0);	q2.addWait( time );color_by_bottle(q2, 5, true, 10, 0, 0);
					color_by_bottle(q2, 3, true, 200, 0, 0);	q2.addWait( time );color_by_bottle(q2, 3, true, 10, 0, 0);
/*
					
					list[ 1].setColor(q2, top, 200, 0, 0, 0);	q2.addWait( time );list[ 1].setColor(q2, top, 0, 0, 0, 0);	
					list[ 3].setColor(q2, top, 200, 0, 0, 0);	q2.addWait( time );list[ 3].setColor(q2, top, 0, 0, 0, 0);	
					list[ 5].setColor(q2, top, 200, 0, 0, 0);	q2.addWait( time );list[ 5].setColor(q2, top, 0, 0, 0, 0);	
					list[ 7].setColor(q2, top, 200, 0, 0, 0);	q2.addWait( time );list[ 7].setColor(q2, top, 0, 0, 0, 0);	
					list[ 9].setColor(q2, top, 200, 0, 0, 0);	q2.addWait( time );list[ 9].setColor(q2, top, 0, 0, 0, 0);	
					list[11].setColor(q2, top, 200, 0, 0, 0);	q2.addWait( time );list[11].setColor(q2, top, 0, 0, 0, 0);	
					list[ 9].setColor(q2, top, 200, 0, 0, 0);	q2.addWait( time );list[ 9].setColor(q2, top, 0, 0, 0, 0);	
					list[ 7].setColor(q2, top, 200, 0, 0, 0);	q2.addWait( time );list[ 7].setColor(q2, top, 0, 0, 0, 0);	
					list[ 5].setColor(q2, top, 200, 0, 0, 0);	q2.addWait( time );list[ 5].setColor(q2, top, 0, 0, 0, 0);	
					list[ 3].setColor(q2, top, 200, 0, 0, 0);	q2.addWait( time );list[ 3].setColor(q2, top, 0, 0, 0, 0);	
*/
				}
				return q2;
			}
		});
	}
	public void strobo(final Queue q, int repeat) {
		int time =5000;
		while (repeat-- > 0){
			turnOffLeds(q);
			if(time>0){
				q.addWait(	time / 100 );
			}
			setAllLeds( q, "ff", 255, 255, 255, 255);
			time=time-10;
		}
	}
	public void nazmiane(final Queue q, final int repeat, final int time ) {
		final int colile = 40;

		Initiator.logger.i( "LightsManager", "start nazmiane");
		q.add( new AsyncMessage( true ){
			@Override
			public String getName() {
				return "nazmiane";
			}
			@Override
			public Queue run(Mainboard dev, Queue queue) {
				Initiator.logger.i( "LightsManager", "start2 nazmiane");
				Queue q2 = new Queue();
				turnOffLeds(q2);
				for (int k = 0;k<repeat;k++){
					int i=0;
					for (;i<255;i+=colile){
						for(int j=0;j<12;j++){
							int a	= i + 40;
							int b	= 255 - i + 1;
							color_by_bottle( q2, j, true, a, 0, b );
							color_by_bottle( q2, j, false, b, 0, a);
						}
					}
					q2.addWait( time );
					for (i=255;i>=0;i-=colile){
						for(int j=0;j<12;j++){
							int a	= i + 40;
							int b	= 255 - i + 1;
							color_by_bottle( q2, j, true, a, 0, b );
							color_by_bottle( q2, j, false, b, 0, a);
						}
					}
					q2.addWait( time );
				} 
				Initiator.logger.i( "LightsManager", "koniec fadeButelka");
				return q2;
			}
		});
	}

	public void linijka(final Queue q, final int repeat, final int time) {
		q.add( new AsyncMessage( true ){
			@Override
			public String getName() {
				return "linijka";
			}
			@Override
			public Queue run(Mainboard dev, Queue queue) {
				Queue q2 = new Queue();
				Initiator.logger.i( "LightsManager", "start2 linijka");
				turnOffLeds( q2 );
				for (int i=0;i<3620;i+=10){
					color_by_bottle(q2, 0, true, 0, 0, flag( i + 00 ) );
					color_by_bottle(q2, 1, true, 0, 0, flag( i + 30 ) );
					color_by_bottle(q2, 2, true, 0, 0, flag( i + 60 ) );
					color_by_bottle(q2, 3, true, 0, 0, flag( i + 90 ) );
					color_by_bottle(q2, 4, true, 0, 0, flag( i + 120 ) );
					color_by_bottle(q2, 5, true, 0, 0, flag( i + 150 ) );
					color_by_bottle(q2, 6, true, 0, 0, flag( i + 180 ) );
					color_by_bottle(q2, 7, true, 0, 0, flag( i + 210 ) );
					color_by_bottle(q2, 8, true, 0, 0, flag( i + 240 ) );
					color_by_bottle(q2, 9, true, 0, 0, flag( i + 270 ) );
					color_by_bottle(q2, 10, true, 0, 0, flag( i + 300 ) );
					color_by_bottle(q2, 11, true, 0, 0, flag( i + 310 ) );
					
					/*
					list[ 0].setColor(q2, true, 0,0, flag( i + 00 ), 0);
					list[ 1].setColor(q2, true, 0,0, flag( i + 30 ), 0);
					list[ 2].setColor(q2, true, 0,0, flag( i + 60 ), 0);
					list[ 3].setColor(q2, true, 0,0, flag( i + 90 ), 0);
					list[ 4].setColor(q2, true, 0,0, flag( i + 120 ), 0);
					list[ 5].setColor(q2, true, 0,0, flag( i + 150 ), 0);	
					list[ 6].setColor(q2, true, 0,0, flag( i + 180 ), 0);
					list[ 7].setColor(q2, true, 0,0, flag( i + 210 ), 0);
					list[ 8].setColor(q2, true, 0,0, flag( i + 240 ), 0);
					list[ 9].setColor(q2, true, 0,0, flag( i + 270 ), 0);
					list[10].setColor(q2, true, 0,0, flag( i + 300 ), 0);
					list[11].setColor(q2, true, 0,0, flag( i + 310 ), 0);*/
				}
				q2.addWait( time );
				Initiator.logger.i( "LightsManager", "koniec fadeButelka");
				return q2;
			}
		});
	}

	public void flaga(final Queue q, final int repeat, final int time) {
		q.add( new AsyncMessage( true ){
			@Override
			public String getName() {
				return "flaga";
			}
			@Override
			public Queue run(Mainboard dev, Queue queue) {
				Queue q2 = new Queue();
				Initiator.logger.i( "LightsManager", "start2 flaga");
				turnOffLeds( q2 );

				for (int i=0;i<2620;i+=20){
				//	Initiator.logger.i( "LightsManager", "aaaa: " + a);
					color_by_bottle(q2, 0, true, flag( i + 00 ), 0, flag( i + 180 ));
					color_by_bottle(q2, 1, true, flag( i + 30 ), 0, flag( i + 210 ));
					color_by_bottle(q2, 2, true, flag( i + 60 ), 0, flag( i + 240 ));
					color_by_bottle(q2, 3, true, flag( i + 90 ), 0, flag( i + 270 ));
					color_by_bottle(q2, 4, true, flag( i + 120 ), 0, flag( i + 300 ));
					color_by_bottle(q2, 5, true, flag( i + 150 ), 0, flag( i + 330 ));	
					color_by_bottle(q2, 6, true, flag( i + 180 ), 0, flag( i + 00 ));
					color_by_bottle(q2, 7, true, flag( i + 210 ), 0, flag( i + 30 ));
					color_by_bottle(q2, 8, true, flag( i + 240 ), 0, flag( i + 60 ));
					color_by_bottle(q2, 9, true, flag( i + 270 ), 0, flag( i + 90 ));
					color_by_bottle(q2, 10, true, flag( i + 300 ), 0, flag( i + 120 ));
					color_by_bottle(q2, 11, true, flag( i + 310 ), 0, flag( i + 150 ));		

				}
				q2.addWait( time );
				Initiator.logger.i( "LightsManager", "koniec fadeButelka");
				return q2;
			}
		});
	}

	public void tecza(final Queue q, final int repeat) {
		q.add( new AsyncMessage( true ){
			@Override
			public String getName() {
				return "tecza";
			}
			@Override
			public Queue run(Mainboard dev, Queue queue) {
				Queue q2 = new Queue();
				Initiator.logger.i( "LightsManager", "start2 tecza");
				turnOffLeds( q2 );
				int i=0;
				for (;i<4620;i+=40){
					color_by_bottle(q2, 0, true, flag( i + 00 ), 0, flag( i + 180 ));
					color_by_bottle(q2, 1, true, flag( i + 30 ), 0, flag( i + 210 ));
					color_by_bottle(q2, 2, true, flag( i + 60 ), 0, flag( i + 240 ));
					color_by_bottle(q2, 3, true, flag( i + 90 ), 0, flag( i + 270 ));
					color_by_bottle(q2, 4, true, flag( i + 120 ), 0, flag( i + 300 ));
					color_by_bottle(q2, 5, true, flag( i + 150 ), 0, flag( i + 330 ));	
					color_by_bottle(q2, 6, true, flag( i + 180 ), 0, flag( i + 00 ));
					color_by_bottle(q2, 7, true, flag( i + 210 ), 0, flag( i + 30 ));
					color_by_bottle(q2, 8, true, flag( i + 240 ), 0, flag( i + 60 ));
					color_by_bottle(q2, 9, true, flag( i + 270 ), 0, flag( i + 90 ));
					color_by_bottle(q2, 10, true, flag( i + 300 ), 0, flag( i + 120 ));
					color_by_bottle(q2, 11, true, flag( i + 310 ), 0, flag( i + 150 ));		

					color_by_bottle(q2, 0, false, flag( i + 00 ), 0, flag( i + 180 ));
					color_by_bottle(q2, 1, false, flag( i + 30 ), 0, flag( i + 210 ));
					color_by_bottle(q2, 2, false, flag( i + 60 ), 0, flag( i + 240 ));
					color_by_bottle(q2, 3, false, flag( i + 90 ), 0, flag( i + 270 ));
					color_by_bottle(q2, 4, false, flag( i + 120 ), 0, flag( i + 300 ));
					color_by_bottle(q2, 5, false, flag( i + 150 ), 0, flag( i + 330 ));	
					color_by_bottle(q2, 6, false, flag( i + 180 ), 0, flag( i + 00 ));
					color_by_bottle(q2, 7, false, flag( i + 210 ), 0, flag( i + 30 ));
					color_by_bottle(q2, 8, false, flag( i + 240 ), 0, flag( i + 60 ));
					color_by_bottle(q2, 9, false, flag( i + 270 ), 0, flag( i + 90 ));
					color_by_bottle(q2, 10, false, flag( i + 300 ), 0, flag( i + 120 ));
					color_by_bottle(q2, 11, false, flag( i + 310 ), 0, flag( i + 150 ));		
				}
				Initiator.logger.i( "LightsManager", "koniec fadeButelka");
				return q2;
			}
		});
	}

	public static int flag(int degree ){
		int a	= (int) (Math.sin( Math.toRadians(degree) ) * 127 + 127);
		return a;
	}

	public void zapal(Queue q) {		
		setAllLeds( q, "ff", 255, 255, 255, 255);
		Initiator.logger.i( "LightsManager", "koniec zapal");
	}

	public void mrygajRGB(Queue q, int repeat, int time  ) {
		turnOffLeds(q);
		while (repeat-- > 0){

			q.addWait( time/2 );
			setAllLeds( q, "01", 255, 100, 0, 0);
			q.addWait( time/2 );
			setAllLeds( q, "01", 255, 255, 0, 0);

			q.addWait(time/2 );
			setAllLeds( q, "02", 255, 0, 100, 0);
			q.addWait( time/2 );
			setAllLeds( q, "02", 255, 0, 255, 0);

			q.addWait( time/2 );
			setAllLeds( q, "02", 255, 0, 0, 100);
			q.addWait( time/2 );
			setAllLeds( q, "02", 255, 0, 0, 255);
		}
	}

	public void scann_leds( Queue q ){
		boolean isNewVersion = true;
		if(isNewVersion){
	
		}else{
			LedOrder lo = new LedOrder();
			lo.asyncStart(barobot, q);
			q.add( new AsyncMessage( true ){
				@Override	
				public String getName() {
					return "onReady LedOrder" ;
				}
				@Override
				public Queue run(Mainboard dev, Queue queue) {
					System.out.println(" run scann_leds");
					Upanel[] up		= barobot.i2c.getUpanels();
					for(int i =0; i<up.length;i++){
						Upanel uu = up[i];
						System.out.println("+Upanel "
								+ "dla butelki: " + uu.getBottleNum() 
								//+ " w wierszu " + uu.getRow()
								+ " pod numerem " + uu.getNumInRow()
								//+ " o indeksie " + uu.getRow()
								+ " ma adres " + uu.getAddress() );
					}
					barobot.i2c.reloadIndex();
					barobot.ledsReady	= true;
					Queue q3	= new Queue();
					setAllLeds( q3, "ff", 255, 255, 255, 255);
					q3.addWait(200);
					setAllLeds( q3, "ff", 0, 0, 0, 0);
					return q3;
				}
			});
		}
	}

	public void carret_color( Queue q, int red, int green, int blue ){
		if( barobot.newLeds ){
			String color = String.format("%02x", red ) 			// 24 bit color
					+ String.format("%02x", green )
					+ String.format("%02x", blue );
	
			q.add("l00," + color, true);// 00 = led address left		
			q.add("l01," + color, true);// 01 = led address right
		}else{
			Carret cc =  new Carret(Constant.cdefault_index, Constant.cdefault_address);	
			if(red == 0 && green == 0 && blue == 0 ){
				cc.addLed(q, "ff", 0 );
			}else{
				cc.addLed(q, "ff", 255 );
			}
		}
	}

	public void color_by_bottle( Queue q, int bottleNum, boolean topBottom, int red, int green, int blue ){
		if( barobot.newLeds ){
			int id = -1;
			if( bottleNum >= 0 && bottleNum < bottom_index.length ){
				if( topBottom == true ){		// top
					id = top_index[bottleNum];
				}else{
					id = bottom_index[bottleNum];
				}
				if(id != 0){				// lnn,color i.e:   l0100FFFFFF
					String command = "l" + id + "," 
							+ String.format("%02x", red ) 
							+ String.format("%02x", green )
							+ String.format("%02x", blue  );
					q.add(command, true);
				}
			}
		}else{
			Upanel up	= barobot.i2c.getUpanelByBottle(bottleNum);
			if( up != null ){
				up.setColor(q, topBottom, red, green, blue, 0);
			}
		}
	}

	public void setLedsByBottle(Queue q, int bottleNum, String string, int value, int red, int green, int blue, boolean addToQueue ) {
		if( barobot.newLeds ){
			String color = String.format("%02x", red ) 
					+ String.format("%02x", green )
					+ String.format("%02x", blue );
	
			if( bottleNum >= 0 && bottleNum < top_index.length ){
				int id_top	= top_index[bottleNum];
				int id_bt	= bottom_index[bottleNum];
				if(id_top > -1 ){
					String command = "l" + id_top + "," + color;
					if(addToQueue){
						q.add(command, true);
					}else{
						barobot.mb.send(command+"\n");
					}
				}
				if(id_bt > -1 ){
					String command = "l" + id_bt + "," + color;
					if(addToQueue){
						q.add(command, true);
					}else{
						barobot.mb.send(command+"\n");
					}
				}
			}
		}else{
			Upanel up	= barobot.i2c.getUpanelByBottle(bottleNum);
			if(up!=null){
				if(addToQueue){
					up.setLed(q, string, value);
				}else{
					barobot.mb.send("L"+ up.getAddress() + ","+string+","+value+"\n");			// i.e in calibration
				}	
			}
		}
	}

	public void setAllLeds( Queue q, String string, int value, int red, int green, int blue ) {
		if( barobot.newLeds ){
			String command = "Q00"
					+ String.format("%02x", red ) 
					+ String.format("%02x", green )
					+ String.format("%02x", blue  );
			q.add(command, true);
		}else{
			Queue q1		= new Queue();	
			Upanel[] up		= barobot.i2c.getUpanels();
			for(int i =0; i<up.length;i++){
				up[i].setLed(q1, string, value);
			}
			q.add(q1);
		}
	}
	public void turnOffLeds(Queue q){
		setAllLeds( q, "ff", 0, 0, 0, 0);
	}

	public void setBottomLeds(Queue q, int red, int green, int blue) {
		for(int i =0; i<bottom_leds.length;i++){
			int led = bottom_leds[i];
			String command = "l" + led + ","
					+ String.format("%02x", red ) 
					+ String.format("%02x", green )
					+ String.format("%02x", blue  );
			q.add(command, true);
		}
	}

}

