package m209;

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

単純な配列実装ではなくヒープを用いたダイクストラ法を実装し、取り出しコストを対数時間に抑えることで大規模グラフにも対応可能な構成とした。  
距離配列に無限大相当の初期値を与え、始点のみ0としておき、優先度付きキューから最小距離頂点を順次取り出す典型的な設計である。  
ヒープから古い距離を持つ頂点が取り出される可能性に対しては、配列 ar_D と取り出した距離を比較してスキップすることで対処した。  
重み付き隣接リストは「行き先頂点と重み」のペアを列挙する形で保持し、各辺を高々1回緩和するとともに無向グラフでは逆向き辺も同様に登録した。  

// 感想をこの上の行までに記述する */ //注意：この行を改変すると感想がないと見なされます


public class DijkstrasAlgorithmWithHeap extends Frame{
	private static final String str_datafile = "src/m209/ex209_data_stationgraph_weightedadjlist.csv";
	private static final String str_datafile2 = "src/m209/ex209_data_stationgraph_name.csv";
	private static final String str_datafile3 = "src/m209/ex209_data_stationgraph_pos.csv";
	private static final String str_datafile5 = "src/m209/ex209_data_stationgraph_edge.csv";
	
	public static void main(String[] args){
		
		//* 重みが非負なので「貪欲」特性のため、再挿入する必要がない
		
		
		// 作成したメソッドのテストを行うメソッドです。
		// このメソッドがfalseを出力した場合、解答のメソッドは正しく設計されていません。
		// ただし、falseが出力されなかったとしても正解とは限りません。
		test();
		
		// 実データの描画
		//new DijkstrasAlgorithmWithHeap();
	}
	
	
	/* αコメントをこの下の行から記述する //注意：この行を改変するとαコメントがないと見なされます
	
	measure は重み付き隣接リスト list_adjlist と始点 initnode を受け取り、各頂点への最短距離配列 ar_D を返す。  
    各頂点の距離を初期値として正の無限大に設定し、始点のみ 0 にした上で、距離、頂点を要素とする最小ヒープを用いてダイクストラ法を実行する。  
    ヒープから取り出した頂点 v について、取り出し距離が ar_D[v] より大きい場合は古い情報として無視し、
    隣接頂点 u に対する緩和 ar_D[u] > ar_D[v] + weight のときにのみ距離とヒープを更新する。  
	
	// αコメントをこの上の行までに記述する */ //注意：この行を改変するとαコメントがないと見なされます
	private static int[] measure(ArrayList<ArrayList<ArrayList<Integer>>> list_adjlist, int initnode){

		//* グラフの頂点数を取得し、距離配列を「無限大」で初期化
	    int n = list_adjlist.size();
	    int[] ar_D = new int[n];
	    for(int i = 0; i < n; i++){
	        ar_D[i] = Integer.MAX_VALUE;
	    }
	    
	    //* 始点の距離を 0 に設定し、ヒープへ挿入
	    ar_D[initnode] = 0;
	    MyHeap heap = new MyHeap();
	    heap.insert(initnode, 0);
	    
	    //* ヒープが空になるまで最短候補頂点を取り出して緩和を繰り返す
	    while(heap.size() > 0){
	    	
	    	//* 現在の最小距離を持つ頂点情報を取得（ヒープが空なら終了）
	        ArrayList<Integer> minData = heap.extractMin();
	        if(minData == null){
	            break;
	        }
	        
	        //* 頂点 v とその距離 distV を取り出し、古い距離であればスキップ
	        int v = minData.get(0);
	        int distV = minData.get(1);
	        if(distV > ar_D[v]){
	            continue;
	        }
	        
	        //* v に隣接する頂点 u への各重み付き辺を走査して距離の緩和を試みる
	        ArrayList<ArrayList<Integer>> adjVertices = list_adjlist.get(v);
	        for(int i = 0; i < adjVertices.size(); i++){
	            ArrayList<Integer> edge = adjVertices.get(i);
	            int u = edge.get(0);
	            int weight = edge.get(1);
	            
	            //* より短い経路が見つかった場合のみ距離を更新し、ヒープへ再登録
	            if(ar_D[u] > ar_D[v] + weight){
	                ar_D[u] = ar_D[v] + weight;
	                heap.insert(u, ar_D[u]);
	            }
	        }
	    }
	    
	    //* 始点から各頂点への最短距離を格納した配列を返却
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
	
	//データ呼び出し
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
	private void drawDijkstrasAlgorithmWithHeap(){
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
	
	public DijkstrasAlgorithmWithHeap(){
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
		drawDijkstrasAlgorithmWithHeap();
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


