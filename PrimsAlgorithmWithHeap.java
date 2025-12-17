package m210;

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
import java.util.HashMap;

/* 感想をこの下の行から記述する //注意：この行を改変すると感想がないと見なされます

最小全域木の構成手続として、Prim 法をヒープ付き優先度付きキューで実装し、各反復で最小重みの枝を効率的に選択できる構成とした。  
頂点ごとに木への取り込みコスト D を管理し、確定済み集合から外部への枝のうち最小重みのものを逐次選択することで、貪欲に全域木を拡張している。  
ヒープには (頂点, D 値) の組を格納し、既により小さい D が確定している頂点が取り出された場合はスキップすることで古い情報を自然に無効化している。  
辺の数を E、頂点数を V とすると、各頂点の挿入更新や extractMin のコストは対数時間に抑えられるため、
全体の計算量はおおよそ O(E log V) と評価される。  

// 感想をこの上の行までに記述する */ //注意：この行を改変すると感想がないと見なされます

public class PrimsAlgorithmWithHeap extends Frame{
	private static final String str_datafile2 = "src/m210/ex210_data_stationgraph_name.csv";
	private static final String str_datafile3 = "src/m210/ex210_data_stationgraph_pos.csv";
	private static final String str_datafile4 = "src/m210/ex210_data_stationgraph_weightedadjlist.csv";
	private static final String str_datafile5 = "src/m210/ex210_data_stationgraph_edge.csv";
	
	public static void main(String[] args){
		
		//* (1) bが確定後にc経由の枝重みがより小さくなる場合
		//* (2) 確定済みbのDとTが更新され最小全域木にならない
		
		// 作成したメソッドのテストを行うメソッドです。
		// このメソッドがfalseを出力した場合、解答のメソッドは正しく設計されていません。
		// ただし、falseが出力されなかったとしても正解とは限りません。
		test();
		
		// 実データの描画 // 完成したメソッドによる全国の鉄道網の全域木を描画
		//new PrimsAlgorithmWithHeap();
	}
	
	
	/* αコメントをこの下の行から記述する //注意：この行を改変するとαコメントがないと見なされます
	
	span は重み付き隣接リスト list_adjlist、開始頂点 initnode、反復回数 repeatednum を受け取り、最小全域木の先行点配列 ar_T を返す。  
    各頂点 v に対して木への取り込みコスト D[v] を保持し、確定済み集合 ar_fixed[v] が 
    false の頂点のうち D[v] が最小のものをヒープから取り出して木に追加する。  
    追加した頂点 v に隣接する頂点 u について、u が未確定かつ現在のコスト D[u] より枝重み 
    w が小さい場合に D[u] と先行点 T[u] を更新し、(u, D[u]) をヒープへ再登録する。  
    repeatednum は木に追加する頂点数の上限（通常 V−1 回の辺選択に相当）として機能し、
    ヒープ操作を用いることで計算量は辺数 E と頂点数 V に対しておおよそ O(E log V) となる。
	
	// αコメントをこの上の行までに記述する */ //注意：この行を改変するとαコメントがないと見なされます
	private static int[] span(ArrayList<ArrayList<ArrayList<Integer>>> list_adjlist, int initnode, int repeatednum){
		
		//* 頂点数を取得し、先行点配列 ar_T・コスト配列 ar_D・確定フラグ ar_fixed を初期化
	    int nodenum = list_adjlist.size();
	    int[] ar_T = new int[nodenum];
	    int[] ar_D = new int[nodenum];
	    boolean[] ar_fixed = new boolean[nodenum];
	    for (int v = 0; v < nodenum; v++) {
	        ar_T[v] = -1;
	        ar_D[v] = Integer.MAX_VALUE;
	        ar_fixed[v] = false;
	    }

	    //* 開始頂点のコストを 0 に設定し、(頂点, コスト) をヒープへ挿入
	    ar_D[initnode] = 0;
	    MyHeap heap = new MyHeap();
	    heap.insert(initnode, 0);

	    //* 木に追加した頂点数を管理しながら Prim 法のメインループを実行
	    int step4count = 0;
	    while (heap.size() > 0 && step4count < repeatednum) {

	    	//* 現在最小コストの頂点データを取り出し、空であれば終了
	        ArrayList<Integer> data = heap.extractMin();
	        if (data == null)	break;

	        //* 取り出した頂点 v が既に木に含まれていればスキップ
	        int v = data.get(0);
	        if (ar_fixed[v])	continue;

	        //* 頂点 v を木に確定させ、隣接リストから候補辺を調べる
	        ar_fixed[v] = true;
	        ArrayList<ArrayList<Integer>> list_adj = list_adjlist.get(v);
	        for (ArrayList<Integer> nodeinfo : list_adj) {
	            int u = nodeinfo.get(0);
	            int w = nodeinfo.get(1);

	            //* 既に木に含まれる頂点には遷移しない
	            if (ar_fixed[u])	continue;

	            //* より軽い枝が見つかった場合、u のコストと先行点を更新しヒープへ挿入
	            if (ar_D[u] > w) {
	                ar_D[u] = w;
	                ar_T[u] = v;
	                heap.insert(u, ar_D[u]);
	            }
	        }
	        //* 木に新たな頂点を 1 つ追加したとみなしカウンタを増加
	        step4count++;
	    }
	    //* 構成した最小全域木の先行点配列を返却
	    return ar_T;
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
		int initnode1test1 = 0;
		int repeatednum1test1 = nodenumtest1-1;
		int[] ar_predtest1ans1 = {-1, 0, 1, 2, 2};
		boolean test1res1 = false;
		int[] ar_predtest1res1 = span(list_adjlisttest1, initnode1test1, repeatednum1test1);
		//System.out.println(Arrays.toString(ar_predtest1res1) + " " + Arrays.toString(ar_predtest1ans1));
		test1res1 = Arrays.equals(ar_predtest1res1, ar_predtest1ans1);
		System.out.println(test1res1);
		
		//test2
		int nodenumtest2 = 8;
		int[][] ar_edgetest2 = {{0, 1, 20}, {0, 2, 9}, {0, 3, 8}, {1, 4, 15}, {2, 3, 6}, {2, 5, 7}, {3, 5, 15}, {3, 6, 10}, {4, 5, 18}, {5, 7, 22}};
		ArrayList<ArrayList<ArrayList<Integer>>> list_adjlisttest2 = getAdjListFromWeightedEdges(ar_edgetest2, nodenumtest2, false);
		//System.out.println(list_adjlisttest2);
		//test2-1
		int initnode1test2 = 0;
		int repeatednum1test2 = nodenumtest2-1;
		int[] ar_predtest2ans1 = {-1, 4, 3, 0, 5, 2, 3, 5};
		boolean test2res1 = false;
		int[] ar_predtest2res1 = span(list_adjlisttest2, initnode1test2, repeatednum1test2);
		//System.out.println(Arrays.toString(ar_predtest2res1) + " " + Arrays.toString(ar_predtest2ans1));
		test2res1 = Arrays.equals(ar_predtest2res1, ar_predtest2ans1);
		System.out.println(test2res1);
		
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
		int nodenumtest0 = 8825;
		int initnode1test0 = 0;
		int repeatednum1test0 = nodenumtest0-1;
		int[] ar_predtest0res1 = span(list_adjlisttest0, initnode1test0, repeatednum1test0);
		//System.out.println(Arrays.toString(ar_predtest0res1));
		int weight1test0 = getWeightFromPredecessors(list_adjlisttest0, ar_predtest0res1);
		System.out.println("全域木の重み： 20324295 m, " + (weight1test0==20324295)); 
		
	}
	
	// 先行点を格納した配列から重みの総和を取得するメソッド
	private static int getWeightFromPredecessors(ArrayList<ArrayList<ArrayList<Integer>>> list_adjlist, int[] ar_T){
		// 頂点数
		int nodenum = ar_T.length;
		// 全域木の重みをar_Tから求める
		int weight = 0;
		for(int node1 = 0; node1 < nodenum; node1++){
			// node1の先行点
			int node0 = ar_T[node1];
			// node1の先行点が存在しない
			if(node0 == -1){
				continue;
			}
			// node1の隣接リスト
			ArrayList<ArrayList<Integer>> list_adj = list_adjlist.get(node0);
			for(ArrayList<Integer> list_nodeinfo : list_adj){
				if(list_nodeinfo.get(0) == node1){
					weight += list_nodeinfo.get(1);
					break;
				}
			}
		}
		
		return weight;
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
			File file = new File(str_datafile4);
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
	private void drawPrimsAlgorithmWithHeap(){
		//
		//データ呼び出し
		ArrayList<ArrayList<ArrayList<Integer>>> list_adjlist = getData4();
		ArrayList<Double> list_pos = getData3();
		
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
		
		
		// グラフの全枝の描画
		//ArrayList<Integer> list_edge = getData5();
		//Chart1.drawEdgeBatch(new Color(250, 0, 0), ar_VtxDPos, list_edge);
		
		// 全域木の枝の描画
		ArrayList<ArrayList<ArrayList<Integer>>> list_adjlisttest0 = getData4();
		ArrayList<String> list_name = getData2();
		int nodenumtest0 = 8825;
		int initnode1test0 = 1026;// 東京
		int repeatednum1test0 = nodenumtest0-1;
		int[] ar_predtest0res1 = span(list_adjlisttest0, initnode1test0, repeatednum1test0);
		//
		ArrayList<Integer> list_MSTedge = new ArrayList<Integer>();
		for(int a1 = 0; a1 < ar_predtest0res1.length; a1++){
			// プリムの方法の実行開始頂点
			if(a1 == initnode1test0){
				continue;
			}
			list_MSTedge.add(a1);
			list_MSTedge.add(ar_predtest0res1[a1]);
		}
		Chart1.drawEdgeBatch(new Color(150, 150, 150), ar_VtxDPos, list_MSTedge);
		
		
		// 頂点描画
		Color ColVtx;
		int size1 = 1;
		for(int a1 = 0; a1 < ar_VtxDPos.length; a1++){
			ColVtx = new Color(200, 255, 200);
			DoublePos VtxDPos = new DoublePos(ar_VtxDPos[a1].PosX, ar_VtxDPos[a1].PosY);
			Chart1.drawVertex(ColVtx, VtxDPos, size1, true, true);
		}
		
		//
		repaint();
	}
	
	public PrimsAlgorithmWithHeap(){
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
		drawPrimsAlgorithmWithHeap();
	}
	
	public void paint(Graphics g){
		g.drawImage(BufImg, 0, 0, this);
	}
	
	
}


class MyHeap{
	// ヒープ本体
	static ArrayList<ArrayList<Integer>> list_heap;
	// ヒープのキー（ヒープ内の頂点の格納位置（インデックス））の管理
	static HashMap<Integer, Integer> hmap_key2index;
	
	public MyHeap(){
		// 初期化
		list_heap = new ArrayList<ArrayList<Integer>>();
		hmap_key2index = new HashMap<Integer, Integer>();
	}
	
	// インデックスindex1の頂点の（より小さい数値をもつ）子供のインデックスを取得する
	private static int getChildIndex(int index1){
		//左の子供のインデックスを取得する
		int leftchildindex = getLeftChildIndex(index1);
		// ヒープの大きさ
		int length = list_heap.size();
		// 左の子供が存在しない（＝子供が存在しない）場合
		if(length <= leftchildindex){
			return -1;
		}
		// 右の子供のインデックスを取得する
		int rightchildindex = getRightChildIndex(index1);
		// 左の子供のみ存在する場合
		if(length <= rightchildindex){
			//左の子供のインデックスを返す
			return leftchildindex;
		}
		// 左と右の子供のうち小さい数値を格納している子供のインデックスを返す
		if(compare(list_heap.get(leftchildindex).get(1), list_heap.get(rightchildindex).get(1)) == true){
			return leftchildindex;
		}
		return rightchildindex;
	}
	
	// ヒープに格納されている要素（データ）の数を取得する
	public int size(){
		return list_heap.size();
	}
	
	// ヒープに格納されている要素（データ）を出力する
	public void output(){
		System.out.println(list_heap); 
	}
	
	// 2つの子供・親子に格納される数値の大小関係を調べる
	private static boolean compare(int val1, int val2){
		return (val1 <= val2);
	}
	
	//インデックスindex1の頂点の親のインデックスを取得する
	private static int getParentIndex(int index1){
		if(index1 <= 0){
			return -1;
		}
		return (int)((index1-1) / 2);
	}
	
	
	//インデックスindex1の頂点の左の子のインデックスを取得する
	private static int getLeftChildIndex(int index1){
		return 2 * index1 + 1;
	}
	
	//インデックスindex1の頂点の右の子のインデックスを取得する
	private static int getRightChildIndex(int index1){
		return 2 * index1 + 2;
	}
	
	// 最小値を持つデータの取り出し
	public ArrayList<Integer> extractMin(){
		// ヒープが空の場合
		if(size() == 0){
			return null;
		}
		
		// 先頭（根）のデータ（＝このデータの数値が最小値）を取り出す（返り値）
		ArrayList<Integer> list_rootdata = list_heap.get(0);
		// ヒープ内の頂点の格納位置を削除
		hmap_key2index.remove(list_rootdata.get(0));
		// 最後尾の要素（データ）を先頭に移す
		ArrayList<Integer> tail1 = list_heap.get(list_heap.size()-1);
		list_heap.set(0, tail1);
		// 最後尾の要素（データ）を削除する
		list_heap.remove(list_heap.size()-1);
		// 先頭の値を下移動する
		movedownward(0);
		// 最小値を持つデータを返す
		return list_rootdata;
	}
	
	// 挿入
	public void insert(int key1, int value1){
		// ヒープに挿入するデータ（キー、値）作成
		ArrayList<Integer> list_data = new ArrayList<Integer>();
		list_data.add(key1);
		list_data.add(value1);
		// まだ登録されていないキーの場合
		if(hmap_key2index.containsKey(key1) == false){
			// 最後尾にデータを挿入する
			list_heap.add(list_data);
			// 挿入した最後尾のデータを上移動する
			int index1 = list_heap.size();
			moveupward(index1-1);
			return;
		}
		
		// 既に登録されているキーの場合
		// 当該のキーの現在のヒープ内の位置（インデックス）を取得する
		int valueindex = hmap_key2index.get(key1);
		//
		ArrayList<Integer> list_currentdata = list_heap.get(valueindex);
		// 当該のキーの現在の数値との大小を比較して更新する必要があるかどうか調べる
		// 更新不要の場合
		if(compare(list_currentdata.get(1), list_data.get(1)) == true){
			return;
		}
		// 更新が必要となる場合
		// 要素（データ）更新
		list_heap.set(valueindex, list_data);
		// 上移動させる
		moveupward(valueindex);
	}
	
	// インデックスindex1の位置の要素（データ）を下移動する
	private static void movedownward(int index1){
		// 要素（データ）が存在しない場合
		if(list_heap.size() == 0){
			return;
		}
		// 上移動させる頂点（インデックスindex1に格納されている頂点）に格納されているデータを取得する
		ArrayList<Integer> list_data = list_heap.get(index1);
		// 下移動させる頂点のデータの数値
		int val1 = list_data.get(1);
		// 下移動させる頂点のデータのキー
		int key1 = list_data.get(0);
		//インデックスindex1の頂点の子供（ただし、2つ子供がある場合は、より小さい数値を格納している子供）のインデックスを取得する
		int childindex = getChildIndex(index1);
		
		// 子供がいなくなるまで頂点を下に移動させる（下移動）
		while(childindex != -1){
			// 下に移動させている頂点に格納されているデータの数値とその子供に格納されているデータの数値を比較し、
			// 下に移動が必要ない場合は下移動を終了する
			if(compare(val1, list_heap.get(childindex).get(1)) == true){
				break;
			}
			// index1の頂点に子供のデータを格納する（ヒープの要素の更新）
			update(index1, list_heap.get(childindex));
			
			// 次に下に移動させる頂点のインデックス（childindex）を代入する
			index1 = childindex;
			// 次に下に移動させる頂点の子供（ただし、2つ子供がある場合は、より小さいデータの数値を格納している子供）のインデックスを取得する
			childindex = getChildIndex(index1);
		}
		// 下移動完了時に、移動が完了した頂点に移動させた頂点のデータ（メソッド呼び出し時のindex1のデータ）を代入する
		update(index1, list_data);
		//
		return;
	}
	
	// インデックスindex1の位置の要素（データ）を上移動する
	private static void moveupward(int index1){
		// 要素（データ）が存在しない場合
		if(list_heap.size() == 0){
			return;
		}
		// 上移動させる頂点（インデックスindex1に格納されている頂点）に格納されているデータを取得する
		ArrayList<Integer> list_data = list_heap.get(index1);
		// 上移動させる頂点のデータの数値
		int val1 = list_data.get(1);
		// 上移動させる頂点のデータのキー
		int key1 = list_data.get(0);
		//インデックスindex1の頂点の親のインデックスを取得する
		int parentindex = getParentIndex(index1);
		
		// 親がいなくなるまで頂点を上に移動させる（上移動）
		while(parentindex != -1){
			// 上に移動させている頂点に格納されているデータの数値とその親に格納されているデータの数値を比較し、
			// 上に移動が必要ない場合は上移動を終了する
			if(compare(list_heap.get(parentindex).get(1), val1) == true){
				 break;
			}
			// index1の頂点に親のデータを格納する（ヒープの要素の更新）
			update(index1, list_heap.get(parentindex));
			
			// 次に上に移動させる頂点のインデックス（parentindex）を代入する
			index1 = parentindex;
			// 次に上に移動させる頂点の親のインデックスを取得する
			parentindex = getParentIndex(index1);
		}
		// 上移動完了時に、移動が完了した頂点に移動させた頂点のデータ（メソッド呼び出し時のindex1のデータ）を代入する
		update(index1, list_data);
		//
		return;
	}
	
	// ヒープのインデックスindex1の位置の要素（データ）を更新する
	private static void update(int index1, ArrayList<Integer> list_data1){
		// ヒープの要素を更新する
		list_heap.set(index1, list_data1);
		// ヒープ内のキーの格納位置を更新する
		hmap_key2index.put(list_data1.get(0), index1);
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

