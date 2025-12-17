package p204;

//グラフ描画用
import java.awt.Frame;
//ファイル操作用
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/* 感想をこの下の行から記述する //注意：この行を改変すると感想がないと見なされます

到達可能性判定を幅優先探索で実装し、入力検証と早期終了条件を明示した。  
隣接リストは疎グラフにおいて走査効率が高く、メモリ効率も良好であると評価できる。  
対して隣接行列は辺存在判定が定数時間であり、密グラフや反復判定に利点がある。  
境界条件（空、範囲外、同一点）を先行して処理し、例外的経路を早期に打ち切った。  
計算量は探索部が O(V+E)、補助構造は visited と待ち行列により O(V) と見積もられる。  

// 感想をこの上の行までに記述する */ //注意：この行を改変すると感想がないと見なされます




public class Reachability extends Frame{
	private static final String str_datafile5 = "src/p204/ex204_data_dblpgraph_edge.csv";
	private static final String str_datafile2 = "src/p204/ex204_data_dblpgraph_name.csv";
	
	
	public static void main(String[] args)	{
		
		//* 最大次数をKとすると、到達可能性判定は隣接リストで O(n+m)=O(n+K n) と小さく、
		//* 疎グラフに適するため採用した。
		
		
		
		// 作成したメソッドのテストを行うメソッドです。
		// このメソッドがfalseを出力した場合、解答のメソッドは正しく設計されていません。
		// ただし、falseが出力されなかったとしても正解とは限りません。
		test();
		
		
	}
	
	/* αコメントをこの下の行から記述する //注意：この行を改変するとαコメントがないと見なされます
	
	枝集合からグラフを生成し、始点 nodeS から終点 nodeT への到達可能性を判定する。  
	check は入力検証（null、ノード数非正、範囲外、同一ノード）を先に処理し、違反時は即時返却とする。  
	到達判定は幅優先探索で行い、visited により重複展開を抑止し、待ち行列で層状に走査する。  
	隣接表現としては疎グラフに適した隣接リストを主に用い、必要に応じて隣接行列生成も備える。  
	探索中に nodeT を検出した時点で真を返し、キューが尽きれば偽を返す構成とする。
	
	// αコメントをこの上の行までに記述する */ //注意：この行を改変するとαコメントがないと見なされます
	private static boolean check(int[][] ar_edge, int nodenum, int nodeS, int nodeT){

        //* 入力検証
        if (ar_edge == null || nodenum <= 0) return false;

        //* 始点・終点の範囲検査（範囲外は偽）
        if (nodeS < 0 || nodeS >= nodenum || nodeT < 0 || nodeT >= nodenum) return false;

        //* 自己到達は真
        if (nodeS == nodeT) return true;

        //* 枝集合から隣接リストを構築
        ArrayList<ArrayList<Integer>> adj = getAdjListFromEdges(ar_edge, nodenum);

        //* 到達管理配列と待ち行列の用意
        boolean[] visited = new boolean[nodenum];
        Queue<Integer> q = new LinkedList<>();

        //* 始点を訪問済みに設定し探索開始
        visited[nodeS] = true;
        q.add(nodeS);

        //* 幅優先の主ループ（キューが空になるまで層状展開）
        while (!q.isEmpty()) {
            //* 先頭頂点を取得して隣接を走査
            int u = q.poll();
            for (int v : adj.get(u)) {
                //* 未訪問なら到達判定、終点一致で即時真を返す
                if (!visited[v]) {
                    if (v == nodeT) return true;
                    //* 到達印を付してキューへ追加
                    visited[v] = true;
                    q.add(v);
                }
            }
        }
        //* キューが尽きるまで未検出であれば不可達として偽
        return false;
	}
	
	
	// 枝の集合から隣接リストを取得するメソッド
	private static ArrayList<ArrayList<Integer>> getAdjListFromEdges(int[][] ar_edge, int nodenum){
		// 隣接リストを初期化（全てを空のリストにする）
		ArrayList<ArrayList<Integer>> list_adjlist1 = new ArrayList<ArrayList<Integer>>();
		for(int a1 = 0; a1 < nodenum; a1++){
			list_adjlist1.add(new ArrayList<Integer>());
		}
		// 枝を追加
		for(int a1 = 0; a1 < ar_edge.length; a1++){
			// 枝の両端点
			int node1 = ar_edge[a1][0]; 
			int node2 = ar_edge[a1][1];
			list_adjlist1.get(node1).add(node2);
		}
		// 隣接リストを返す
		return list_adjlist1;
	}
	
	// 枝の集合から隣接行列を取得するメソッド
	private static ArrayList<ArrayList<Integer>> getAdjMatrixFromEdges(int[][] ar_edge, int nodenum){
		// 隣接行列を初期化（全てを0にする）
		ArrayList<ArrayList<Integer>> list_adjmatrix1 = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> list1 = new ArrayList<Integer>();
		for(int a1 = 0; a1 < nodenum; a1++){
			list1 = new ArrayList<Integer>();
			for(int a2 = 0; a2 < nodenum; a2++)
				list1.add(0);
			list_adjmatrix1.add(list1);
		}
		// 枝を追加（枝の値のみ1にする）
		for(int a1 = 0; a1 < ar_edge.length; a1++){
			//枝の両端点
			int node1 = ar_edge[a1][0]; int node2 = ar_edge[a1][1];
			list_adjmatrix1.get(node1).set(node2, 1);
		}
		// 隣接行列を返す
		return list_adjmatrix1;
	}
	
	
	
	// 作成したメソッドのテストを行うメソッドです。
	// このメソッドがfalseを出力した場合、解答のメソッドが正しく設計されていません。
	// ただし、falseが出力されなかったとしても正解とは限りません。
	private static void test() {
		//test1
		int nodenumtest1 = 7;
		int[][] ar_edgetest1 = {{0, 1}, {0, 2}, {0, 3}, {1, 0}, {1, 2}, {1, 3}, {2, 0}, {2, 1}, {3, 1}, {4, 5}, {5, 4}};
		boolean test1res1 = false, test1res2 = false, test1res3 = false;
		//test1-1
		test1res1 = (check(ar_edgetest1, nodenumtest1, 0, 3) == true);
		//test1-2
		test1res2 = (check(ar_edgetest1, nodenumtest1, 4, 5) == true);
		//test1-3
		test1res3 = (check(ar_edgetest1, nodenumtest1, 6, 0) == false);
		System.out.println(test1res1 + " " + test1res2 + " " + test1res3);
		
		//実データを用いたテスト
		//test2();
		
		
		
		
		
	}
	
	//実データを用いたテスト
	private static void test2() {
		System.out.println("DBLPから作成した無向グラフの実データを用いたテスト：");
		System.out.println("ダイクストラから各人物への到達可能性を調べる：");
		//データ呼び出し
		ArrayList<Integer> list_edgedblp = getData5();
		int edgenumdblp = list_edgedblp.size()/2;
		//System.out.println(edgenumdblp);
		int[][] ar_edgedblp = new int[edgenumdblp][];
		for(int a1 = 0; a1 < edgenumdblp; a1++){
			int edgeindex1 = a1;
			ar_edgedblp[edgeindex1] = new int[2];
			ar_edgedblp[edgeindex1][0] = list_edgedblp.get(2 * a1);
			ar_edgedblp[edgeindex1][1] = list_edgedblp.get(2 * a1 + 1);
		}
		ArrayList<String> list_namedblp = getData2();
		//
		int[] ar_bignameid = {
			76301, // Dijkstra, Edsger W.
			17761, // Allen, Paul G.
			61721, // Chomsky, Noam
			101320, // Gates, William H.
			221204, // Nash, John F., Jr.
			222865, // Neumann, John von
			260936, // Roth, Alvin E.
			276038, // Shapley, Lloyd S.
			309300 // Turing, Alan M.
		};
		String[] ar_memo = {
			"計算機科学者。ダイクストラ。オランダ人",
			"マイクロソフトの創業者。沈没した軍艦を引き上げたりしている方の人",
			"色々なところに顔を出す言語学者",
			"マイクロソフトの創業者",
			"1994年のノーベル経済学賞の受賞者",
			"ノイマン型計算機の考案者",
			"2012年のノーベル経済学賞の受賞者",
			"2012年のノーベル経済学賞の受賞者",
			"計算機の原モデルを考案した数学者"
		};
		boolean[] ar = {
			true, 
			true, 
			true, 
			true, 
			false, 
			true, 
			true, 
			true, 
			false
		};
		int nodenumdblp = 365826;
		for(int a1 = 1; a1 < ar_bignameid.length; a1++){
			System.out.println(list_namedblp.get(ar_bignameid[a1]) + " " + ar_memo[a1] + " " + (check(ar_edgedblp, nodenumdblp, ar_bignameid[0], ar_bignameid[a1])==ar[a1]));
		}
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
			while((line = br.readLine()) != null){
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
			}catch (Exception e){
				System.out.println(e.getMessage());
			}
		}
		return list_data;
	}
	
}

