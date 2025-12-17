package m207;

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
import java.util.LinkedList;
import java.util.Queue;

/* 感想をこの下の行から記述する //注意：この行を改変すると感想がないと見なされます

連結成分の抽出を幅優先探索で実装し、未訪問頂点から成分ごとに走査して重複追加を排した。   
各成分は探索完了時に ArrayList<Integer> として収集し、比較検証のため辞書順で正規化して整列する。  
境界条件（空グラフ、範囲外参照）を避けるため、null/空検査と安全な添字取得を前提とした記述とした。  
実データでは -1 番兵で行末を表現する可変長隣接を採用し、I/O 例外時は処理継続可能なログ出力とした。  

// 感想をこの上の行までに記述する */ //注意：この行を改変すると感想がないと見なされます



public class ConnectedComponents{
	private static final String str_datafile = "src/m207/ex207_data_dblpgraph_adjlist.csv";
	private static final String str_datafile2 = "src/m207/ex207_data_dblpgraph_name.csv";
	
	public static void main(String[] args){
		
		//* 訪問配列visitedで未訪問頂点から探索し，各成分で頂点を一度だけcomponentに追加
		//* 各頂点を一度だけ処理すれば重複が生じず，計算量もO(n+m)に抑えられるため
		
		// 作成したメソッドのテストを行うメソッドです。
		// このメソッドがfalseを出力した場合、解答のメソッドは正しく設計されていません。
		// ただし、falseが出力されなかったとしても正解とは限りません。
		test();
	}
	
	
	
	/* αコメントをこの下の行から記述する //注意：この行を改変するとαコメントがないと見なされます
	
	get は無向グラフの隣接リスト list_adjlist を入力として、各連結成分を要素とするリストを返す。  
    訪問配列 visited を用い、未訪問頂点 v から BFS を開始して同一成分の頂点を component に収集する。  
    成分抽出が終わるたびに component を結果リストに追加し、全頂点を一度ずつ処理する設計とする。
	
	// αコメントをこの上の行までに記述する */ //注意：この行を改変するとαコメントがないと見なされます
	private static ArrayList<ArrayList<Integer>> get(ArrayList<ArrayList<Integer>> list_adjlist){
		//* 頂点数 n を取得し、結果と訪問配列を初期化
		int n = list_adjlist.size();
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        boolean[] visited = new boolean[n];

        //* 未訪問頂点ごとに BFS を行い、1 つの連結成分を収集
        for (int v = 0; v < n; v++) {
            if (!visited[v]) {
                ArrayList<Integer> component = new ArrayList<>();
                //* 成分収集のための BFS 呼び出し
                search(v, list_adjlist, visited, component); // BFSで成分収集
                //* 収集済み成分を結果へ追加
                result.add(component);
            }
        }
        //* 抽出した全連結成分のリストを返却
        return result;
	}
	
	
	/* αコメントをこの下の行から記述する //注意：この行を改変するとαコメントがないと見なされます
	
	search は始点 start から幅優先探索を行い、到達可能な頂点を component に記録する補助手続である。  
    待ち行列 q により層状に走査し、未訪問の隣接 w を訪問済みにして順次投入する。  
    記録は訪問順に行い、visited 配列で重複展開を抑止する。BFS の性質上、各頂点と各辺は高々一度処理される。
	
	// αコメントをこの上の行までに記述する */ //注意：この行を改変するとαコメントがないと見なされます
	private static void search(int start, ArrayList<ArrayList<Integer>> adj, boolean[] visited, ArrayList<Integer> component){
		//* 始点を訪問済みにして待ち行列へ投入
		Queue<Integer> q = new LinkedList<>();
        visited[start] = true;
        q.add(start);

        //* BFS ループ（キューが尽きるまで成分を拡張）
        while (!q.isEmpty()) {
        	//* 先頭 u を取り出して成分に追加
        	int u = q.poll();
            component.add(u);
            
            //* 隣接頂点を走査し、未訪問のみ到達を確定
            ArrayList<Integer> neighbors = adj.get(u);
            for (int i = 0; i < neighbors.size(); i++) {
                int w = neighbors.get(i);
                if (!visited[w]) {
                	//* 訪問印を付けてキューへ追加
                    visited[w] = true;
                    q.add(w);
                }
            }
        }
	}
	
	
	// 作成したメソッドのテストを行うメソッドです。
	// このメソッドがfalseを出力した場合、解答のメソッドが正しく設計されていません。
	// ただし、falseが出力されなかったとしても正解とは限りません。
	private static void test(){
		//test1
		int nodenumtest1 = 7;
		int[][] ar_edgetest1 = {{0, 1}, {0, 2}, {1, 2}, {2, 4}, {3, 5}};
		ArrayList<ArrayList<Integer>> list_adjlisttest1 = getAdjListFromEdges(ar_edgetest1, nodenumtest1, false);
		//System.out.println(list_adjlisttest1);
		ArrayList<ArrayList<Integer>> list_cctest1ans1 = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> list1 = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 4));
		list_cctest1ans1.add(list1);
		list1 = new ArrayList<Integer>(Arrays.asList(3, 5));
		list_cctest1ans1.add(list1);
		list1 = new ArrayList<Integer>(Arrays.asList(6));
		list_cctest1ans1.add(list1);
		//System.out.println(list_cctest1ans1);
		
		//test1-1
		boolean test1res1 = false;
		ArrayList<ArrayList<Integer>> list_cctest1res1 = get(list_adjlisttest1);
		//System.out.println(list_cctest1res1);
		for(int i = 0; i < list_cctest1res1.size(); i++){
			Collections.sort(list_cctest1res1.get(i));
		}
		Collections.sort(list_cctest1res1, new ForComp());
		test1res1 = list_cctest1res1.equals(list_cctest1ans1);
		System.out.println(test1res1);
		
		
		//実データを用いたテスト
		//test2();
		
	}
	
	// 枝の集合から隣接リストを取得するメソッド
	private static ArrayList<ArrayList<Integer>> getAdjListFromEdges(int[][] ar_edge, int nodenum, boolean directed){
		//隣接リストを初期化（全てを0にする）
		ArrayList<ArrayList<Integer>> list_adjlist1 = new ArrayList<ArrayList<Integer>>();
		for(int a1 = 0; a1 < nodenum; a1++){
			list_adjlist1.add(new ArrayList<Integer>());
		}
		//枝を追加
		for(int a1 = 0; a1 < ar_edge.length; a1++){
			//枝の両端点
			int node1 = ar_edge[a1][0]; int node2 = ar_edge[a1][1];
			list_adjlist1.get(node1).add(node2);
			// 無向グラフの場合、逆向きの枝を加える
			if(directed == false){
				list_adjlist1.get(node2).add(node1);
			}
		}
		//隣接リストを返す
		return list_adjlist1;
	}
	
	
	//実データを用いたテスト
	private static void test2(){
		System.out.println("DBLPから作成した無向グラフの実データを用いたテスト：");
		
		//データ呼び出し
		ArrayList<ArrayList<Integer>> list_adjlistdblp = getData();
		ArrayList<String> list_namedblp = getData2();
		//
		ArrayList<ArrayList<Integer>> list_ccdblp = get(list_adjlistdblp);
		for(int i = 0; i < list_ccdblp.size(); i++){
			Collections.sort(list_ccdblp.get(i));
		}
		Collections.sort(list_ccdblp, new ForComp());
		
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
		
		System.out.println("連結成分数：" + list_ccdblp.size() + " " + (list_ccdblp.size()==4573));
		System.out.println(list_ccdblp.get(0).size()==347886);
		System.out.println((list_ccdblp.get(4528).size()==3) + " " + list_ccdblp.get(4528) + " " + list_ccdblp.get(4528).contains(309300));
		System.out.println((list_ccdblp.get(4558).size()==1) + " "+ list_ccdblp.get(4558) + " " + list_ccdblp.get(4558).contains(221204));
		
	}
	
	//csvファイルの実データ呼び出し
	private static ArrayList<ArrayList<Integer>> getData(){
		//
		ArrayList<ArrayList<Integer>> list_data = new ArrayList<ArrayList<Integer>>();
		//
		BufferedReader br = null;
		try{
			File file = new File(str_datafile);
			br = new BufferedReader(new FileReader(file));
			String line;
			String[] ar1;
			ArrayList<Integer> list1;
			while((line = br.readLine()) != null){
				ar1 = line.split(",");
				//
				list1 = new ArrayList<Integer>();
				for(int a1 = 0; a1 < ar1.length; a1++){
					int a2 = Integer.parseInt(ar1[a1]);
					if(a2 == -1){
						break;
					}
					list1.add(a2);
				}
				list_data.add(list1);
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
		}catch (Exception e){
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
