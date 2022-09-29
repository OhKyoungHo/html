package project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PanelI extends JPanel  {

	//객체 생성
	JPanel p = new JPanel();
	//배열의 내용 직접 선언
	//음식명
	String [] menuName =  {"까르보나라","봉골레파스타","로제파스타","오믈렛","필라프","리조또","스테이크","토마토 스파게티"};
	//가격
	String  [] price =  {"8000원","8000원","10000원","8000원","8000원","8000원","12000원","8000원"}; 
	//버튼
	JButton [] iBtn = new JButton[8];  //


	int count = 0;  //???

	MainTest parent;

	int sum = 0;	//??

	public PanelI(MainTest mainTest) {
		//MainTest에서 panelA = new PanelA(this); 형태로 썼기 때문에 
		// 이를 받기위해서 입력매개변수를  MainTest mainTest로 씀
		parent = mainTest;		//주소값만 받는다.
		setBackground(new Color(250,250,250));
		addLayout();			//화면 출력 메소드 호출
		eventProc();			//이벤트 발생 메소드 호출
	}

	//화면에 출력할 메소드 생성
	void addLayout() {
		setLayout(new BorderLayout()); 
		JPanel pCenter = new JPanel(new GridLayout(3,3));	// 행,열에 맞춰 배치
		pCenter.setPreferredSize(new java.awt.Dimension(500,500));//크기조절
		add(pCenter,BorderLayout.CENTER);                           //판넬 가운데로 
		for(int i=0;i<iBtn.length;i++) {		
			//반복문 및 배열을 이용해서 이미지 저장 
			String imgSrc = "src\\project\\img\\"+String.valueOf(i)+".jpg";
			//반복문 및 배열을 이용해서 버튼에 메뉴 이름 및 가격 입력             
			String name = menuName[i] +" / "+ price[i];            

			ImageIcon icon = new ImageIcon(imgSrc);               
			Image resizeImage = imageResize(icon, 200, 200);    //그림 크기 설정
			iBtn[i] = new JButton(name, new ImageIcon(resizeImage));
			iBtn[i].setHorizontalTextPosition(JButton.CENTER);  //버튼에 있는 글 중간에 위치시키기    
			iBtn[i].setVerticalTextPosition(JButton.BOTTOM); //글자를 수직위치 조정으로 그림 밑으로 보내기
			iBtn[i].setBackground(new Color(250,250,250));	//버튼 색깔 설정
		}
		//반복문을 이용해서  패널에 붙이기
		for(int i=0; i<iBtn.length ; i++) {
			pCenter.add(iBtn[i]);
		}    
	}
	//이벤트발생 메소드 생성
	void eventProc() {

		//메뉴 버튼을 눌렀을 때
		for(int i=0;i<iBtn.length;i++) {	//반복문 사용
			iBtn[i].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					// 이벤트가 발생한 컴포넌트의 참조를 얻어옴.
					JButton eBtn = (JButton)e.getSource();
					String[] strArr = eBtn.getText().split(" / ");	//받아온 값을 "/" 기준으로 나눠서 배열하기
					inputData(strArr); //VO에 입력값 넘김
					clearTA();     	   //ta 지워주는 메소드 호출    
					showUp();         //ta에 주문 내용 출력
				}//end of actionPerformed
			});//end of addActionListener
		}//for

		//전체취소 버튼을 눌렀을 때
		parent.cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				parent.list.clear();	//parent.list의 내용 지우기
				clearTA();		 //ta 지워주는 메소드 호출  
			}//end of actionPerformed
		});//end of addActionListener

		//결제 버튼을 눌렀을때
		parent.pay.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//조건문 설정을 위한 showConfirmDialog에 대한 변수값 qut_data 설정
				int qut_data = JOptionPane.showConfirmDialog(p, "주문을 결제하시겠습니까??","주문내역", 
						JOptionPane.YES_NO_CANCEL_OPTION);
				if(qut_data == 0) { //[예] 버튼
					System.out.println(p.toString());
					JOptionPane.showMessageDialog(p, "결제되었습니다."+"\n"+"[주문내역]\n" 
							+ String.valueOf(parent.list.size())+"개" + " " + String.valueOf(parent.sum)+"원" );
				}
				else if(qut_data == 1) { //[아니오] 버튼

				}
				else if(qut_data == 2){ //[취소] 버튼

				}

			}
		});

	}//eventProc

	//버튼에 들어갈 아이콘 이미지 크기 변환 메소드
	Image imageResize(ImageIcon icon, int x, int y){
		Image image = icon.getImage();//아이콘에서 이미지를 불러옴
		Image resizeImage = image.getScaledInstance(x,y,java.awt.Image.SCALE_SMOOTH); //이미지 화질을 최대한 보존하면서 크기 조정
		return resizeImage;//크기 조정된 이미지 반환
	}
	//클릭한 값을 PayVO에 저장하기
	void inputData(String[] strArr) {
		PayVO p = new PayVO();

		p.setMenu(strArr[0]);
		p.setPrice(strArr[1]);

		parent.list.add(p);
	}

	void clearTA() {
		//각 TextArea의 내용을 비움
		parent.taMenu.setText(null);
		parent.taPrice.setText(null);

		//최종 금액, 개수 초기화
		parent.sum = 0;
		parent.sumCount.setText(String.valueOf(parent.list.size())+"개");
		parent.sumPrice.setText(String.valueOf(parent.sum)+"원");
	}
	//총 주문 갯수와 금액을 출력하는 메소드
	void showUp() {
		String input = null;
		for(PayVO vo : parent.list) {
			input = vo.toString();
			String[] arr = input.split("/");
			parent.taMenu.append(arr[0]+"\n");
			parent.taPrice.append(arr[2]+"\n");
			calSum(arr[2]);

			parent.sumCount.setText(String.valueOf(parent.list.size())+"개");
			parent.sumPrice.setText(String.valueOf(parent.sum)+"원");
		}
	}//showUp
	//원이 있으면 없으면 if(index ==-1) 경우로 가서 출력
	//가격에 있는 
	void calSum(String price) {
		int index = price.indexOf("원");
		if(index ==-1)
		{parent.sum += Integer.parseInt(price);	// 가격 String배열의 숫자만 계산하여 sum에 저장
		return;
		} else
			//원이 있으면 해당 경우 출력
		{parent.sum += Integer.parseInt(price.substring(0, index));
		}
	}


}


