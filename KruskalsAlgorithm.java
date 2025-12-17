package m211;

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
import java.util.Collections;
import java.util.Comparator;

/* 感想をこの下の行から記述する //注意：この行を改変すると感想がないと見なされます

クラスカル法では全ての枝を重み順にソートし、サイクルを作らないように軽い枝から
順に全域木へ追加していくという非常にシンプルな貪欲戦略で最小全域木が得られる点が分かりやすかった。  
素直にサイクル判定を行うと計算量が大きくなってしまうが、本課題の実装のように
素集合データ構造を用いることで、集合の結合と代表元の問い合わせをほぼ一定時間で処理できるのが印象的だった。    
Prim 法と比較すると、クラスカル法は枝リストから直接処理できるため、
疎なグラフや枝列があらかじめ与えられている状況に向いている一方で、ヒープ付き Prim 法は隣接リストからのインクリメンタルな構築に適していると感じた。 

// 感想をこの上の行までに記述する */ //注意：この行を改変すると感想がないと見なされます




public class KruskalsAlgorithm extends Frame{
	private static final String str_datafile2 = "src/m211/ex211_data_stationgraph_name.csv";
	private static final String str_datafile3 = "src/m211/ex211_data_stationgraph_pos.csv";
	private static final String str_datafile5 = "src/m211/ex211_data_stationgraph_edge.csv";
	private static final String str_datafile6 = "src/m211/ex211_data_stationgraph_weightededge.csv";
	
	public static void main(String[] args){
		
		// 作成したメソッドのテストを行うメソッドです。
		// このメソッドがfalseを出力した場合、解答のメソッドは正しく設計されていません。
		// ただし、falseが出力されなかったとしても正解とは限りません。
		test();
		
		// 実データの描画  // 完成したメソッドによる首都圏の鉄道網の全域木を描画
		new KruskalsAlgorithm();
	}
	
	
	/* αコメントをこの下の行から記述する //注意：この行を改変するとαコメントがないと見なされます
	
	span は枝集合 ar_edge と頂点数 nodenum、および選択する枝の最大本数 repeatednum を受け取り、
	クラスカル法により構成した全域木の枝集合を ArrayList として返す。  
	最初に sortEdges で枝を重みの昇順に並べ替え、その後、Union-Find 構造 
	MyUnionFind を用いて各枝の端点が属する連結成分を管理しながら、サイクルを生じない枝のみを順に選択していく。  
	repeatednum は選択する枝の上限として機能し、通常は nodenum−1 を指定することで最小全域木を構成できるが、
	より小さい値を指定することで軽い枝だけを部分的に取得することもできる。  
	
	// αコメントをこの上の行までに記述する */ //注意：この行を改変するとαコメントがないと見なされます
	private static ArrayList<ArrayList<Integer>> span(int[][] ar_edge, int nodenum, int repeatednum){
		
		//* 選択された枝を格納するリスト ans を初期化し、まず枝を重み順にソートする
		ArrayList<ArrayList<Integer>> ans = new ArrayList<ArrayList<Integer>>();
		sortEdges(ar_edge);

		//* nodenum 個の頂点を持つ Union-Find 構造を用意し、まだ枝を一つも選んでいない状態から開始する
		MyUnionFind uf = new MyUnionFind(nodenum);

		//* 選択する枝本数 repeatednum が 0 以下ならば空の全域木をそのまま返す
		if (repeatednum <= 0)	return ans;

		//* これまでに選択した枝の本数をカウントする変数 count を 0 で初期化
		int count = 0;

		//* ソート済みの枝列を軽い順に走査し、サイクルを作らない枝を順に追加していく
		for (int i = 0; i < ar_edge.length; i++) {
			
			//* 既に required 本数の枝を選び終えていればループを打ち切る
			if (count >= repeatednum)	break;
			
			//* i 番目の枝の端点 u, v を取得し、それぞれが属する集合の代表元 repU, repV を求める
			int u = ar_edge[i][0];
			int v = ar_edge[i][1];
			int repU = uf.find(u);
			int repV = uf.find(v);

			//* repU と repV が異なる場合のみ、枝 (u, v) を採用して 2 つの集合を結合する
			if (repU != repV) {
				ArrayList<Integer> edge = new ArrayList<Integer>();
				edge.add(u);
				edge.add(v);
				ans.add(edge);
				
				//* 枝 (u, v) を追加したので Union-Find 上でも 2 つの連結成分を union で併合する
				uf.union(u, v);
			}
			
			//* 採用した枝の本数をインクリメント
			count++;
		}
		
		//* クラスカル法により選択された枝集合 ansを返却
		return ans;		
	}
	
	// 枝を重み順でソートするメソッド
	private static void sortEdges(int[][] ar_edge){
		Arrays.sort(ar_edge, (a, b) -> a[1] - b[1]);
		Arrays.sort(ar_edge, (a, b) -> a[0] - b[0]);
		Arrays.sort(ar_edge, (a, b) -> a[2] - b[2]);
	}
	
	
	
	// 作成したメソッドのテストを行うメソッドです。
	// このメソッドがfalseを出力した場合、解答のメソッドが正しく設計されていません。
	// ただし、falseが出力されなかったとしても正解とは限りません。
	private static void test() {
		//test1
		int nodenumtest1 = 5;
		int[][] ar_edgetest1 = {{0, 1, 5}, {0, 2, 6}, {1, 2, 1}, {1, 3, 6}, {2, 3, 1}, {2, 4, 2}};
		ArrayList<ArrayList<ArrayList<Integer>>> list_adjlisttest1 = getAdjListFromWeightedEdges(ar_edgetest1, nodenumtest1, false);
		//System.out.println(list_adjlisttest1);
		//test1-1
		int repeatednum1test1 = nodenumtest1-1;
		boolean test1res1 = false;
		ArrayList<ArrayList<Integer>> list_edgetest1ans1 = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> list1 = new ArrayList<Integer>(Arrays.asList(0, 1));
		list_edgetest1ans1.add(list1);
		list1 = new ArrayList<Integer>(Arrays.asList(1, 2));
		list_edgetest1ans1.add(list1);
		list1 = new ArrayList<Integer>(Arrays.asList(2, 3));
		list_edgetest1ans1.add(list1);
		list1 = new ArrayList<Integer>(Arrays.asList(2, 4));
		list_edgetest1ans1.add(list1);
		//System.out.println(list_edgetest1ans1);
		ArrayList<ArrayList<Integer>> list_edgetest1res1 = span(ar_edgetest1, nodenumtest1, repeatednum1test1);
		//System.out.println(list_edgetest1res1);
		for(int i = 0; i < list_edgetest1res1.size(); i++){
			Collections.sort(list_edgetest1res1.get(i));
		}
		Collections.sort(list_edgetest1res1, new ForComp());
		test1res1 = list_edgetest1res1.equals(list_edgetest1ans1);
		//test1-2
		int repeatednum2test1 = nodenumtest1-3;
		boolean test1res2 = false;
		ArrayList<ArrayList<Integer>> list_edgetest1ans2 = new ArrayList<ArrayList<Integer>>();
		list1 = new ArrayList<Integer>(Arrays.asList(1, 2));
		list_edgetest1ans2.add(list1);
		list1 = new ArrayList<Integer>(Arrays.asList(2, 3));
		list_edgetest1ans2.add(list1);
		//System.out.println(list_edgetest1ans2);
		ArrayList<ArrayList<Integer>> list_edgetest1res2 = span(ar_edgetest1, nodenumtest1, repeatednum2test1);
		//System.out.println(list_edgetest1res2);
		for(int i = 0; i < list_edgetest1res2.size(); i++){
			Collections.sort(list_edgetest1res2.get(i));
		}
		Collections.sort(list_edgetest1res2, new ForComp());
		test1res2 = list_edgetest1res2.equals(list_edgetest1ans2);
		System.out.println(test1res1 + " " + test1res2);
		
		//実データを用いたテスト
		test2();
		
	}
	
	
	//実データを用いたテスト
	private static void test2(){
		System.out.println("鉄道網から作成した無向グラフの実データを用いたテスト：");
		//データ呼び出し
		int[][] ar_edge0 = getData6();
		ArrayList<String> list_name = getData2();
		int nodenumtest0 = 490;
		int repeatednum1test0 = 719;
		
		//
		ArrayList<ArrayList<Integer>> list_edgetest0res1 = span(ar_edge0, nodenumtest0, repeatednum1test0);
		for(int i = 0; i < list_edgetest0res1.size(); i++){
			Collections.sort(list_edgetest0res1.get(i));
		}
		Collections.sort(list_edgetest0res1, new ForComp());
		System.out.println((list_edgetest0res1.size()==489) + " " + (list_edgetest0res1.get(0).get(0)==0) + " " + (list_edgetest0res1.get(30).get(0)==12)); 
		
	}
	
	// 枝の集合から重み付き隣接リストを取得するメソッド
	private static ArrayList<ArrayList<ArrayList<Integer>>> getAdjListFromWeightedEdges(int[][] ar_edge, int nodenum, boolean directed){
		//隣接リストを初期化（全てを空のリストにする）
		ArrayList<ArrayList<ArrayList<Integer>>> list_adjlist1 = new ArrayList<ArrayList<ArrayList<Integer>>>();
		ArrayList<ArrayList<Integer>> list_adj1;
		ArrayList<Integer> list1;
		for(int a1 = 0; a1 < nodenum; a1++){
			list_adj1 = new ArrayList<ArrayList<Integer>>();
			list_adjlist1.add(list_adj1);
		}
		//枝を追加
		ArrayList<Integer> list_edge1;//重み付き枝
		for(int a1 = 0; a1 < ar_edge.length; a1++){
			//枝の両端点
			int node1 = ar_edge[a1][0]; int node2 = ar_edge[a1][1];
			//枝の重み
			int weight1 = ar_edge[a1][2];
			list_edge1 = new ArrayList<Integer>(Arrays.asList(node2, weight1));
			list_adjlist1.get(node1).add(list_edge1);
			// 無向グラフの場合、逆向きの枝を加える
			if(directed == false){
				list_edge1 = new ArrayList<Integer>(Arrays.asList(node1, weight1));
				list_adjlist1.get(node2).add(list_edge1);
			}
		}
		//隣接リストを返す
		return list_adjlist1;
	}
	
	//csvファイルの実データ呼び出し
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
	
	//csvファイルの実データ呼び出し
	private static int[][] getData6(){
		//
		ArrayList<Integer> list_data = new ArrayList<Integer>();
		//
		BufferedReader br = null;
		try{
			File file = new File(str_datafile6);
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
		
		//
		int edgenum = list_data.size() / 3;
		int[][] ar_edge = new int[edgenum][3];
		int cnt = 0;
		for(int a1 = 0; a1 < edgenum; a1++){
			for(int a2 = 0; a2 < 3; a2++){
				ar_edge[a1][a2] = list_data.get(cnt);
				cnt++;
			}
		}
		return ar_edge;
	}
	
	//csvファイルの実データ呼び出し
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
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
		
		return list_data;
	}
	
	//csvファイルの実データ呼び出し
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
	private void drawKruskalsAlgorithm(){
		//データ呼び出し
		ArrayList<Double> list_pos = getData3();
		
		//
		int vtxnum = list_pos.size() / 2;
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
		
		
		// グラフの全枝の描画
		//ArrayList<Integer> list_edge = getData5();
		//Chart1.drawEdgeBatch(new Color(250, 0, 0), ar_VtxDPos, list_edge);
		
		// 全域木の枝の描画
		int[][] ar_edge0 = getData6();
		ArrayList<String> list_name = getData2();
		int nodenumtest0 = 490;
		int repeatednum1test0 = 719;
		ArrayList<ArrayList<Integer>> list_edgetest0res1 = span(ar_edge0, nodenumtest0, repeatednum1test0);
		//
		ArrayList<Integer> list_MSTedge = new ArrayList<Integer>();
		for(int a1 = 0; a1 < list_edgetest0res1.size(); a1++){
			list_MSTedge.add(list_edgetest0res1.get(a1).get(0));
			list_MSTedge.add(list_edgetest0res1.get(a1).get(1));
		}
		Chart1.drawEdgeBatch(new Color(150, 150, 150), ar_VtxDPos, list_MSTedge);
		
		
		//頂点描画
		Color ColVtx;
		int size1;
		for(int a1 = 0; a1 < ar_VtxDPos.length; a1++){
			//東京
			if(a1 == 0){
				size1 = 10;
				ColVtx = new Color(255, 50, 50);
			}else{
				size1 = 7;
				ColVtx = new Color(200, 150, 150);
			}
			DoublePos VtxDPos = new DoublePos(ar_VtxDPos[a1].PosX, ar_VtxDPos[a1].PosY);
			Chart1.drawVertex(ColVtx, VtxDPos, size1, true, true);
		}
		
		//
		repaint();
	}
	
	public KruskalsAlgorithm(){
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
		drawKruskalsAlgorithm();
	}
	
	public void paint(Graphics g){
		g.drawImage(BufImg, 0, 0, this);
	}
	
	
}


class MyUnionFind{
	// 集合
	static int[] ar_set;
	// 集合の数
	static int size;
	
	public MyUnionFind(){}
	
	public MyUnionFind(int size1){
		// 集合の数設定（初期化）
		initsize(size1);
		// 集合初期化
		ar_set = new int[size];
		for(int a1 = 0; a1 < size; a1++){
			makeset(a1);
		}
	}
	
	// ファインド（集合の代表元の取得）
	public int find(int node){
		return ar_set[node];
	}
	
	// ユニオン（集合の合併）
	public void union(int node1, int node2){
		// 代表元の取得
		int pid1 = find(node1);
		int pid2 = find(node2);
		// 代表元の更新
		for(int a1 = 0; a1 < size; a1++){
			if(find(a1) == pid2){
				ar_set[a1] = pid1;
			}
		}
	}
	
	// 各要素が属する集合の代表元を出力する
	public void output(){
		System.out.println(Arrays.toString(ar_set)); 
	}
	
	// 代表元の初期化
	private static void makeset(int node1){
		ar_set[node1] = node1;
	}
	
	// 集合の数設定（初期化）
	private static void initsize(int size1){
		size = size1;
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

class ForComp implements Comparator<ArrayList<Integer>>{
	
	@Override
	public int compare(ArrayList<Integer> list1, ArrayList<Integer> list2) {
		int nodenum = list1.size();
		for(int a1 = 0; a1 < nodenum; a1++){
			if(list1.get(a1) < list2.get(a1)){
				return -1;
			}
			else
			if(list1.get(a1) > list2.get(a1)){
				return 1;
			}
		}
		return -1;
	}
}
