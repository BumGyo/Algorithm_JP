package p209;

//グラフ描画用
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
//ファイル操作用
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
//以下のimportの内容について理解できなくても課題を解くのに支障はありません
//テスト用
import java.util.Arrays;

/* 感想をこの下の行から記述する //注意：この行を改変すると感想がないと見なされます

密なグラフを想定して、配列走査に基づく O(n^2) 型のダイクストラ法を用いて最短距離を求める実装とした。  
各反復で全頂点を走査して未確定頂点のうち距離最小の頂点を選択し、その頂点からの辺緩和を行う典型的な配列版アルゴリズムである。  
最小ヒープを用いる実装に比べると頂点数が大きく辺が非常に多い場合でも実装が簡潔であり、配列アクセスのみで完結する利点がある。  
一方でスパースグラフでは O((V+E)logV) のヒープ版の方が計算量の面で優位だが、本課題では密グラフ向けの単純な実装との対比を重視した。 

// 感想をこの上の行までに記述する */ //注意：この行を改変すると感想がないと見なされます

public class DijkstrasAlgorithmInDenseGraphs extends Frame{
	private static final String str_datafile = "src/p209/ex209_data_stationgraph_weightedadjlist.csv";
	private static final String str_datafile2 = "src/p209/ex209_data_stationgraph_name.csv";
	private static final String str_datafile3 = "src/p209/ex209_data_stationgraph_pos.csv";
	private static final String str_datafile5 = "src/p209/ex209_data_stationgraph_edge.csv";
	
	
	public static void main(String[] args){
		
		
		//* 頂点番号が起点からの最短距離順にソートされている場合
		
		
		// 作成したメソッドのテストを行うメソッドです。
		// このメソッドがfalseを出力した場合、解答のメソッドは正しく設計されていません。
		// ただし、falseが出力されなかったとしても正解とは限りません。
		test();
		
		// 実データの描画
		//new DijkstrasAlgorithmInDenseGraphs();
	}
	
	
	/* αコメントをこの下の行から記述する //注意：この行を改変するとαコメントがないと見なされます
	
	measure は重み付き隣接リスト list_adjlist と始点 initnode を受け取り、各頂点への最短距離配列 ar_D を返す。  
    ar_D には始点からの暫定距離を、ar_T には最短経路木上での直前頂点を格納し、fixed 配列で距離が確定した頂点を管理する。  
    各反復で未確定頂点の中から暫定距離が最小の頂点 v を線形探索で選択し、
    v に隣接する頂点 u について ar_D[u] > ar_D[v] + w であれば距離と前駆頂点を更新する。
	
	// αコメントをこの上の行までに記述する */ //注意：この行を改変するとαコメントがないと見なされます
	private static int[] measure(ArrayList<ArrayList<ArrayList<Integer>>> list_adjlist, int initnode){

		//* 頂点数 n を取得し、距離配列 ar_D と前駆頂点配列 ar_T を初期化
	    int n = list_adjlist.size();
	    int[] ar_D = new int[n];
	    int[] ar_T = new int[n];
	    for(int i = 0; i < n; i++){
	        ar_T[i] = -1;
	        ar_D[i] = Integer.MAX_VALUE;
	    }
	    
	    //* 始点の距離を 0 に設定し、確定フラグ配列 fixed を用意
	    ar_D[initnode] = 0;
	    boolean[] fixed = new boolean[n];

	    //* 全頂点が確定するか、これ以上到達可能頂点がないところまで繰り返す
	    for(int j = 0; j < n; j++){

	    	//* 未確定頂点の中から最小距離を持つ頂点 v を線形探索で選択
	        int v = -1;
	        int minDist = Integer.MAX_VALUE;
	        for(int i = 0; i < n; i++){
	            if(!fixed[i] && ar_D[i] < minDist){
	                minDist = ar_D[i];
	                v = i;
	            }
	        }
	        
	        //* 到達可能な未確定頂点が存在しない場合は終了
	        if(v == -1){
	            break;
	        }

	        //* 頂点 v の距離を確定し、隣接頂点に対する緩和処理を行う
	        fixed[v] = true;
	        ArrayList<ArrayList<Integer>> adj = list_adjlist.get(v);
	        for(int k = 0; k < adj.size(); k++){
	            ArrayList<Integer> edge = adj.get(k);
	            int u = edge.get(0);
	            int w = edge.get(1);

	            if(!fixed[u] && ar_D[v] != Integer.MAX_VALUE){
	                if(ar_D[u] > ar_D[v] + w){
	                    ar_D[u] = ar_D[v] + w;
	                    ar_T[u] = v;
	                }
	            }
	        }
	    }
	    
	    //* 始点から各頂点への最短距離を格納した配列 ar_D を返却
	    return ar_D;
	}
	
	
	// 作成したメソッドのテストを行うメソッドです。
	// このメソッドがfalseを出力した場合、解答のメソッドが正しく設計されていません。
	// ただし、falseが出力されなかったとしても正解とは限りません。
	private static void test(){
		//test1
		int nodenumtest1 = 5;
		int[][] ar_edgetest1 = {{0, 1, 5}, {0, 2, 6}, {1, 2, 1}, {1, 3, 6}, {2, 3, 1}, {2, 4, 2}};
		ArrayList<ArrayList<ArrayList<Integer>>> list_adjlisttest1 = getAdjListFromWeightedEdges(ar_edgetest1, nodenumtest1, false);
		//System.out.println(list_adjlisttest1);
		//test1-1
		int[] ar_disttest1ans1 = {0, 5, 6, 7, 8};
		boolean test1res1 = false;
		int[] ar_disttest1res1 = measure(list_adjlisttest1, 0);
		//System.out.println(Arrays.toString(ar_disttest1res1) + " " + Arrays.toString(ar_disttest1ans1));
		test1res1 = Arrays.equals(ar_disttest1res1, ar_disttest1ans1);
		//test1-2
		int[] ar_disttest1ans2 = {8, 3, 2, 3, 0};
		boolean test1res2 = false;
		int[] ar_disttest1res2 = measure(list_adjlisttest1, 4);
		//System.out.println(Arrays.toString(ar_disttest1res2) + " " + Arrays.toString(ar_disttest1ans2));
		test1res2 = Arrays.equals(ar_disttest1res2, ar_disttest1ans2);
		System.out.println(test1res1 + " " + test1res2);
		
		//test2
		int nodenumtest2 = 8;
		int[][] ar_edgetest2 = {{0, 1, 20}, {0, 2, 9}, {0, 3, 8}, {1, 4, 15}, {2, 3, 6}, {2, 5, 7}, {3, 5, 15}, {3, 6, 10}, {4, 5, 18}, {5, 7, 22}};
		ArrayList<ArrayList<ArrayList<Integer>>> list_adjlisttest2 = getAdjListFromWeightedEdges(ar_edgetest2, nodenumtest2, false);
		//System.out.println(list_adjlisttest2);
		//test2-1
		int[] ar_disttest2ans1 = {0, 20, 9, 8, 34, 16, 18, 38};
		boolean test2res1 = false;
		int[] ar_disttest2res1 = measure(list_adjlisttest2, 0);
		//System.out.println(Arrays.toString(ar_disttest2res1) + " " + Arrays.toString(ar_disttest2ans1));
		test2res1 = Arrays.equals(ar_disttest2res1, ar_disttest2ans1);
		//test2-2
		int[] ar_disttest2ans2 = {18, 38, 16, 10, 41, 23, 0, 45};
		boolean test2res2 = false;
		int[] ar_disttest2res2 = measure(list_adjlisttest2, 6);
		//System.out.println(Arrays.toString(ar_disttest2res2) + " " + Arrays.toString(ar_disttest2ans2));
		test2res2 = Arrays.equals(ar_disttest2res2, ar_disttest2ans2);
		System.out.println(test2res1 + " " + test2res2);
		
		//実データを用いたテスト
		test2();
		
	}
	
	
	//実データを用いたテスト
	private static void test2(){
		
		System.out.println("鉄道網から作成した無向グラフの実データを用いたテスト：");
		//データ呼び出し
		ArrayList<ArrayList<ArrayList<Integer>>> list_adjlisttest0 = getData4();
		ArrayList<String> list_name = getData2();
		
		//
		int[] ar_disttest0res1 = measure(list_adjlisttest0, 1026);
		System.out.println("From " + list_name.get(1026)); 
		System.out.println(" to " + list_name.get(58) + ": 1027518 m, " + (ar_disttest0res1[58]==1027518));
		System.out.println(" to " + list_name.get(3918) + ": 1370613 m, " + (ar_disttest0res1[3918]==1370613));
		
	}
	
	// 枝の集合から重み付き隣接リストを取得するメソッド
	private static ArrayList<ArrayList<ArrayList<Integer>>> getAdjListFromWeightedEdges(int[][] ar_edge, int nodenum, boolean directed){
		// 隣接リストを初期化（全てを空のリストにする）
		ArrayList<ArrayList<ArrayList<Integer>>> list_adjlist1 = new ArrayList<ArrayList<ArrayList<Integer>>>();
		ArrayList<ArrayList<Integer>> list_adj1;
		ArrayList<Integer> list1;
		for(int a1 = 0; a1 < nodenum; a1++){
			list_adj1 = new ArrayList<ArrayList<Integer>>();
			list_adjlist1.add(list_adj1);
		}
		// 枝を追加
		ArrayList<Integer> list_edge1;//重み付き枝
		for(int a1 = 0; a1 < ar_edge.length; a1++){
			// 枝の両端点
			int node1 = ar_edge[a1][0]; int node2 = ar_edge[a1][1];
			// 枝の重み
			int weight1 = ar_edge[a1][2];
			// 枝作成して隣接リストに加える
			list_edge1 = new ArrayList<Integer>(Arrays.asList(node2, weight1));
			list_adjlist1.get(node1).add(list_edge1);
			// 無向グラフの場合、逆向きの枝を加える
			if(directed == false){
				list_edge1 = new ArrayList<Integer>(Arrays.asList(node1, weight1));
				list_adjlist1.get(node2).add(list_edge1);
			}
		}
		// 隣接リストを返す
		return list_adjlist1;
	}
	
	//csvファイルの実データ呼び出し
	private static ArrayList<ArrayList<ArrayList<Integer>>> getData4(){
		//
		ArrayList<ArrayList<ArrayList<Integer>>> list_data = new ArrayList<ArrayList<ArrayList<Integer>>>();
		//
		BufferedReader br = null;
		try{
			File file = new File(str_datafile);
			br = new BufferedReader(new FileReader(file));
			String line;
			String[] ar1;
			ArrayList<ArrayList<Integer>> list1;
			ArrayList<Integer> list2;
			while((line = br.readLine()) != null){
				ar1 = line.split(",");
				//
				list1 = new ArrayList<ArrayList<Integer>>();
				list2 = new ArrayList<Integer>();
				for(int a1 = 0; a1 < ar1.length; a1++){
					int a2 = Integer.parseInt(ar1[a1]);
					if(a2 == -1){
						break;
					}
					if(a1 % 2 == 0){
						list2 = new ArrayList<Integer>();
					}
					list2.add(a2);
					if(a1 % 2 == 1){
						list1.add(list2);
					}
				}
				list_data.add(list1);
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}finally{
			try{
				br.close();
			} catch (Exception e){
				System.out.println(e.getMessage());
			}
		}
		
		return list_data;
	}
	
	private static ArrayList<String> getData2(){
		//
		ArrayList<String> list_data = new ArrayList<String>();
		//
		BufferedReader br = null;
		try{
			File file = new File(str_datafile2);
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null){
				list_data.add(line);
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}finally{
			try{
				br.close();
			} catch (Exception e){
				System.out.println(e.getMessage());
			}
		}
		return list_data;
	}
	
	//
	private static ArrayList<Integer> getData5(){
		//
		ArrayList<Integer> list_data = new ArrayList<Integer>();
		//
		BufferedReader br = null;
		try{
			File file = new File(str_datafile5);
			br = new BufferedReader(new FileReader(file));
			String line;
			String[] ar1;
			while ((line = br.readLine()) != null){
				ar1 = line.split(",");
				//
				for(int a1 = 0; a1 < ar1.length; a1++){
					int a2 = Integer.parseInt(ar1[a1]);
					list_data.add(a2);
				}
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}finally{
			try{
				br.close();
			}catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		return list_data;
	}
	
	//
	private static ArrayList<Double> getData3(){
		//
		ArrayList<Double> list_data = new ArrayList<Double>();
		//
		BufferedReader br = null;
		try{
			File file = new File(str_datafile3);
			br = new BufferedReader(new FileReader(file));
			String line;
			String[] ar1;
			while ((line = br.readLine()) != null){
				ar1 = line.split(",");
				//
				for(int a1 = 0; a1 < ar1.length; a1++){
					double a2 = Double.parseDouble(ar1[a1]);
					list_data.add(a2);
				}
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}finally{
			try{
				br.close();
			}catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		return list_data;
	}
	
	//グラフ描画用
	//ウインドウ
	int g_WinWidth = 1000;
	int g_WinHeight = 700;
	Graphics BufGrph;
	Image BufImg;
	//グラフ描画用クラス
	static MyChart Chart1;
	
	//
	private void drawDijkstrasAlgorithmInDenseGraphs(){
		//
		//データ呼び出し
		ArrayList<ArrayList<ArrayList<Integer>>> list_adjlist = getData4();
		ArrayList<Double> list_pos = getData3();
		ArrayList<Integer> list_edge = getData5();
		
		//
		int vtxnum = list_adjlist.size();
		DoublePos[] ar_VtxDPos = new DoublePos[vtxnum];
		double tmpmaxX = -100, tmpmaxY = -100, tmpminX = 1000, tmpminY = 1000;
		for(int a1 = 0; a1 < vtxnum; a1++){
			//頂点データ
			ar_VtxDPos[a1] = new DoublePos(list_pos.get(2*a1), list_pos.get(2*a1+1));
			
			//最大値を求める
			if(tmpmaxY < ar_VtxDPos[a1].PosY){
				tmpmaxY = ar_VtxDPos[a1].PosY;
			}
			if(tmpmaxX < ar_VtxDPos[a1].PosX){
				tmpmaxX = ar_VtxDPos[a1].PosX;
			}
			//最小値を求める
			if(tmpminY > ar_VtxDPos[a1].PosY){
				tmpminY = ar_VtxDPos[a1].PosY;
			}
			if(tmpminX > ar_VtxDPos[a1].PosX){
				tmpminX = ar_VtxDPos[a1].PosX;
			}
		}
		
		//
		Chart1.setTopX(tmpmaxX*1);
		Chart1.setTopY(tmpmaxY*1);
		Chart1.setBaseX(tmpminX*1);
		Chart1.setBaseY(tmpminY*1);
		
		
		//枝描画
		Chart1.drawEdgeBatch(new Color(150, 150, 150), ar_VtxDPos, list_edge);
		
		//頂点描画
		Color ColVtx;
		int size1;
		for(int a1 = 0; a1 < ar_VtxDPos.length; a1++){
			//札幌、東京、鹿児島中央
			if(a1 == 58 || a1 == 1026 || a1 == 3918){
				size1 = 7;
				ColVtx = new Color(255, 150, 150);
			}else{
				size1 = 1;
				ColVtx = new Color(200, 255, 200);
			}
			
			DoublePos VtxDPos = new DoublePos(ar_VtxDPos[a1].PosX, ar_VtxDPos[a1].PosY);
			Chart1.drawVertex(ColVtx, VtxDPos, size1, true, true);
		}
		
		//
		repaint();
	}
	
	public DijkstrasAlgorithmInDenseGraphs(){
		//
		this.setSize(g_WinWidth, g_WinHeight);
		this.setVisible(true);
		//
		BufImg = createImage(getSize().width, getSize().height);
		BufGrph = BufImg.getGraphics();
		
		//グラフ作成
		Chart1 = new MyChart(
			BufGrph, BufImg, 
			1000, 700, 
			50, 0, 1050, 550, 
			20, 20, 50, 20);
		
		
		//描画本体呼び出し
		drawDijkstrasAlgorithmInDenseGraphs();
	}
	
	public void paint(Graphics g){
		g.drawImage(BufImg, 0, 0, this);
	}
	
	
}


//折れ線グラフ描画用クラス
class MyChart{
	//ウインドウサイズ
	int WinWidth;// 
	int WinHeight;//
	//描画初期化
	double BaseX;// 
	double TopX;// 
	double BaseY;// 
	double TopY;// 
	//描画領域
	int LeftMargin;// = 125;
	int RightMargin;// = 125;
	int TopMargin;// = 125;
	int BottomMargin;// = 125;
	//
	int BodyWidth;// 
	int BodyHeight;// 
	//
	Graphics BufGrph;
	Image BufImg;
	
	MyChart(){}
	
	MyChart(
		Graphics n_BufGrph, Image n_BufImg, 
		int n_WinWidth, int n_WinHeight, 
		double n_BaseX, double n_BaseY, 
		double n_TopX, double n_TopY, 
		int n_LeftMargin, int n_RightMargin, int n_TopMargin, int n_BottomMargin
	){
		//
		BufGrph = n_BufGrph;
		BufImg = n_BufImg;
		//ウインドウサイズ
		WinWidth = n_WinWidth;
		WinHeight = n_WinHeight;
		//描画初期化
		BaseX = n_BaseX;
		TopX = n_TopX;
		BaseY = n_BaseY;
		TopY = n_TopY;
		//描画領域
		LeftMargin = n_LeftMargin;
		RightMargin = n_RightMargin;
		TopMargin = n_TopMargin;
		BottomMargin = n_BottomMargin;
		//
		BodyWidth = n_WinWidth - n_LeftMargin - n_RightMargin;
		BodyHeight = n_WinHeight - n_TopMargin - n_BottomMargin;
	}
	
	public void setBaseX(double n_BaseX){
		BaseX = n_BaseX;
	}
	
	public void setBaseY(double n_BaseY){
		BaseY = n_BaseY;
	}
	
	public void setTopX(double n_TopX){
		TopX = n_TopX;
	}
	
	public void setTopY(double n_TopY){
		TopY = n_TopY;
	}
	
	public void setColor(int r1, int b1, int g1){
		BufGrph.setColor(new Color(r1, b1, g1));
	}
	
	public void setColor(Color Col1){
		BufGrph.setColor(Col1);
	}
	
	public IntPos convertPos(DoublePos DPos1){
		double rx1 = (DPos1.PosX - BaseX) / (TopX - BaseX) * (double)BodyWidth;
		double ry1 = (DPos1.PosY - BaseY) / (TopY - BaseY) * (double)BodyHeight;
		
		IntPos PosR = new IntPos(LeftMargin + (int)rx1, TopMargin + BodyHeight - (int)ry1);
		
		return PosR;
	}
	
	//
	public void drawEdgeBatch(Color ColEdge, DoublePos[] ar_VtxDPos, int[] ar_edge){
		//
		BufGrph.setColor(ColEdge);
		//
		DoublePos DPos1, DPos2;
		IntPos Pos1, Pos2;
		for(int a1 = 0; a1 < ar_edge.length-1; a1+=2){
			//枝を構成する点
			int v1 = ar_edge[2*a1];
			int v2 = ar_edge[2*a1+1];
			//
			DPos1 = new DoublePos(ar_VtxDPos[v1].PosX, ar_VtxDPos[v1].PosY);
			Pos1 = convertPos(DPos1);
			DPos2 = new DoublePos(ar_VtxDPos[v2].PosX, ar_VtxDPos[v2].PosY);
			Pos2 = convertPos(DPos2);
			//
			BufGrph.drawLine(Pos1.PosX, Pos1.PosY, Pos2.PosX, Pos2.PosY);
		}
	}
	
	//
	public void drawEdgeBatch(Color ColEdge, DoublePos[] ar_VtxDPos, ArrayList<Integer> list_edge){
		//
		BufGrph.setColor(ColEdge);
		//
		DoublePos DPos1, DPos2;
		IntPos Pos1, Pos2;
		for(int a1 = 0; a1 < list_edge.size(); a1+=2){
			//枝を構成する点
			int v1 = list_edge.get(a1);
			int v2 = list_edge.get(a1+1);
			
			//System.out.println(v1 + " " + v2 );
			
			//
			DPos1 = new DoublePos(ar_VtxDPos[v1].PosX, ar_VtxDPos[v1].PosY);
			Pos1 = convertPos(DPos1);
			DPos2 = new DoublePos(ar_VtxDPos[v2].PosX, ar_VtxDPos[v2].PosY);
			Pos2 = convertPos(DPos2);
			//
			BufGrph.drawLine(Pos1.PosX, Pos1.PosY, Pos2.PosX, Pos2.PosY);
			
		}
		
	}
	
	//
	public void drawVertexBatch(Color ColVertex, DoublePos[] ar_VtxDPos, int size, boolean f_Circle, boolean f_fill){
		//
		BufGrph.setColor(ColVertex);
		//
		DoublePos DPos1;
		IntPos Pos1;
		for(int a1 = 0; a1 < ar_VtxDPos.length; a1++){
			DPos1 = new DoublePos(ar_VtxDPos[a1].PosX, ar_VtxDPos[a1].PosY);
			Pos1 = convertPos(DPos1);
			//点描画
			int dx1 = Pos1.PosX-size/2;
			int dy1 = Pos1.PosY-size/2;
			if(f_Circle){
				if(f_fill){
					BufGrph.fillOval(dx1, dy1, size, size);
				}else{
					BufGrph.drawOval(dx1, dy1, size, size);
				}
			}else{
				if(f_fill){
					BufGrph.fillRect(dx1, dy1, size, size);
				}else{
					BufGrph.drawRect(dx1, dy1, size, size);
				}
			}
		}
		
	}
	
	//
	public void drawVertex(Color ColVertex, DoublePos VtxDPos, int size, boolean f_Circle, boolean f_fill){
		//
		BufGrph.setColor(ColVertex);
		//
		DoublePos DPos1 = new DoublePos(VtxDPos.PosX, VtxDPos.PosY);
		IntPos Pos1 = convertPos(DPos1);
		//点描画
		int dx1 = Pos1.PosX-size/2;
		int dy1 = Pos1.PosY-size/2;
		if(f_Circle){
			if(f_fill){
				BufGrph.fillOval(dx1, dy1, size, size);
			}else{
				BufGrph.drawOval(dx1, dy1, size, size);
			}
		}else{
			if(f_fill){
				BufGrph.fillRect(dx1, dy1, size, size);
			}else{
				BufGrph.drawRect(dx1, dy1, size, size);
			}
		}
	}
	
	//
	public void drawString(Color ColStr, String str1, DoublePos DPos1, int fontsize){
		//
		Font currentFont = BufGrph.getFont();
		Font newFont = currentFont.deriveFont(fontsize);
		BufGrph.setFont(newFont);
		//
		IntPos Pos1 = convertPos(DPos1);
		//
		BufGrph.drawString(str1, Pos1.PosX, Pos1.PosY);
	}
	
	//
	public void drawChart(Color ColChart, DoublePos[] ar_DPos, int recsize){
		
		//折れ線グラフ描画
		BufGrph.setColor(ColChart);
		//点の大きさ
		DoublePos DPos1, DPos2, DPos3, DPos4;
		IntPos Pos1, Pos2, Pos3, Pos4;
		for(int a1 = 0; a1 < ar_DPos.length-1; a1++){
			//
			DPos1 = new DoublePos(ar_DPos[a1].PosX, ar_DPos[a1].PosY);
			DPos2 = new DoublePos(ar_DPos[a1+1].PosX, ar_DPos[a1+1].PosY);
			Pos1 = convertPos(DPos1);
			Pos2 = convertPos(DPos2);
			BufGrph.drawLine(Pos1.PosX, Pos1.PosY, Pos2.PosX, Pos2.PosY);
			//点表示
			BufGrph.fillRect(Pos1.PosX-recsize/2, Pos1.PosY-recsize/2, recsize, recsize);
			
			
			//最後の点表示
			if(a1 >= ar_DPos.length-2){
				BufGrph.setColor(ColChart);
				BufGrph.fillRect(Pos2.PosX-recsize/2, Pos2.PosY-recsize/2, recsize, recsize);
			}
			
		}
		
	}
	
	//
	public void drawChartAxis(Color ColAxis, Color ColSub, String str_LabelX, String str_LabelY, double StepX, double StepY, double LabelStepX, double LabelStepY){
		//軸平行線
		BufGrph.setColor(ColSub);
		//Y軸目盛（X軸平行線）
		DoublePos DPosAX1;
		DoublePos DPosAX2;
		IntPos PosAX1;
		IntPos PosAX2;
		for(double a1 = BaseY+StepY; a1 < TopY; a1+= StepY){
			DPosAX1 = new DoublePos(BaseX, a1);
			DPosAX2 = new DoublePos(TopX, a1);
			PosAX1 = convertPos(DPosAX1);
			PosAX2 = convertPos(DPosAX2);
			BufGrph.drawLine(PosAX1.PosX, PosAX1.PosY, PosAX2.PosX, PosAX2.PosY);
		}
		//X軸目盛（Y軸平行線）
		for(double a1 = (int)BaseX+StepX; a1 < TopX; a1+= StepX){
			DPosAX1 = new DoublePos(a1, BaseY);
			DPosAX2 = new DoublePos(a1, TopY);
			PosAX1 = convertPos(DPosAX1);
			PosAX2 = convertPos(DPosAX2);
			BufGrph.drawLine(PosAX1.PosX, PosAX1.PosY, PosAX2.PosX, PosAX2.PosY);
		}
		
		//軸描画
		BufGrph.setColor(ColAxis);
		//X軸
		DPosAX1 = new DoublePos(BaseX, BaseY);
		DPosAX2 = new DoublePos(BaseX, TopY);
		PosAX1 = convertPos(DPosAX1);
		PosAX2 = convertPos(DPosAX2);
		BufGrph.drawLine(PosAX1.PosX, PosAX1.PosY, PosAX2.PosX, PosAX2.PosY);
		//Y軸
		DoublePos DPosAY1 = new DoublePos(BaseX, BaseY);
		DoublePos DPosAY2 = new DoublePos(TopX, BaseY);
		IntPos PosAY1 = convertPos(DPosAY1);
		IntPos PosAY2 = convertPos(DPosAY2);
		BufGrph.drawLine(PosAY1.PosX, PosAY1.PosY, PosAY2.PosX, PosAY2.PosY);
		//X軸ラベル
		DoublePos DPosAXL = new DoublePos(TopX, BaseY);
		IntPos PosAXL = convertPos(DPosAXL);
		BufGrph.drawString(str_LabelX, PosAXL.PosX, PosAXL.PosY-8);
		//Y軸ラベル
		DoublePos DPosAYL = new DoublePos(BaseX, TopY);
		IntPos PosAYL = convertPos(DPosAYL);
		BufGrph.drawString(str_LabelY, PosAYL.PosX-40, PosAYL.PosY-10);
		//X軸目盛りラベル
		DoublePos DPosOXL;
		IntPos PosOXL;
		for(double a1 = BaseX; a1 < TopX; a1+= LabelStepX){
			DPosOXL = new DoublePos(a1, BaseY);
			PosOXL = convertPos(DPosOXL);
			BufGrph.drawString(a1 + "", PosOXL.PosX-5, PosOXL.PosY+15);
		}
		//Y軸目盛りラベル
		DoublePos DPosOYL;
		IntPos PosOYL;
		//最も長いラベル（最大値）を探す：雑
		int diff = 0;
		for(double a1 = BaseY; a1 < TopY; a1+= LabelStepY){
			String str1 = String.valueOf(a1);
			int a2 = (int)((double)str1.length() * 6.6);
			if(diff < a2){
				diff = a2;
			}
		}
		
		//System.out.println(diff + "<<" + str1.length() + " " + str1);
		for(double a1 = BaseY; a1 < TopY; a1+= LabelStepY){
			DPosOYL = new DoublePos(BaseX, a1);
			PosOYL = convertPos(DPosOYL);
			BufGrph.drawString(a1 + "", PosOYL.PosX-diff, PosOYL.PosY);
		}
		
	}
	
	//凡例（雑）
	public void drawLegend(Color ColString, String[] StrA, Color[] ColA, int recsize){
		//
		DoublePos DPos1;
		IntPos Pos1;
		for(int a1 = 0; a1 < StrA.length; a1++){
			// 説明
			DPos1 = new DoublePos(BaseX, BaseY);
			Pos1 = convertPos(DPos1);
			Pos1 = new IntPos(Pos1.PosX, Pos1.PosY +40 + 15 * a1);
			BufGrph.setColor(ColString);
			BufGrph.drawString(StrA[a1], Pos1.PosX, Pos1.PosY);
			
			//System.out.println(Pos1.PosX + " " + Pos1.PosY);
			// 凡例
			BufGrph.setColor(ColA[a1]);
			BufGrph.fillRect(Pos1.PosX-recsize/2-10, Pos1.PosY-recsize/2-5, recsize, recsize);
			
		}
		
	}
	
}

class IntPos{
	int PosX;
	int PosY;
	
	IntPos(){}
	
	IntPos(int PosX1, int PosY1){
		PosX = PosX1;
		PosY = PosY1;
	}
}

class DoublePos{
	double PosX;
	double PosY;
	
	DoublePos(){}
	
	DoublePos(double PosX1, double PosY1){
		PosX = PosX1;
		PosY = PosY1;
	}
}


