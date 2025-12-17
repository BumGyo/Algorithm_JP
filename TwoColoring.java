package p203;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/* 感想をこの下の行から記述する //注意：この行を改変すると感想がないと見なされます

二部グラフ判定を幅優先探索（BFS）に基づいて実装し、彩色配列による状態管理とキュー駆動の探索制御を整理した。
彩色は未訪問を-1とし、始点に0を割り当てて隣接へ1-色を伝搬する方式とし、衝突（同色隣接）検出で偽を即時返却する設計とした。
連結性を仮定しないため全頂点を起点候補として走査し、各連結成分ごとにBFSを開始する手順により、グラフ全体の正否を確実に判定できることを確認した。

// 感想をこの上の行までに記述する */ //注意：この行を改変すると感想がないと見なされます



public class TwoColoring{
	
	public static void main(String[] args){
		
		
		// 作成したメソッドのテストを行うメソッドです。
		// このメソッドがfalseを出力した場合、解答のメソッドは正しく設計されていません。
		// ただし、falseが出力されなかったとしても正解とは限りません。
		test();
	}
	
	
	
	/* αコメントをこの下の行から記述する //注意：この行を改変するとαコメントがないと見なされます
	
	無向グラフの隣接リスト list_adjlist を入力とし、当該グラフが二部グラフであるかを真偽値で返す。
	判定は幅優先探索（BFS）により行い、彩色配列 color を -1（未訪問）で初期化したうえで、
	各連結成分の代表頂点 s から探索を開始して s に色0を割り当てる。キューから頂点 u を取り出し、
	各隣接 v に対し未彩色なら color[v] を 1 - color[u] として彩色しキューに追加し、
	既に彩色済みで color[v] == color[u] であれば二部条件に反するため即座に false を返却する。
	全探索が衝突なく完了した場合は true を返し、計算量は頂点数 n と枝数 m に対して O(n + m) となる。
	
	// αコメントをこの上の行までに記述する */ //注意：この行を改変するとαコメントがないと見なされます
	private static boolean check(ArrayList<ArrayList<Integer>> list_adjlist){
		//* 頂点数を取得し、未訪問を示す彩色配列colorを-1で初期化する
		int n = list_adjlist.size();
        int[] color = new int[n];
        for (int i = 0; i < n; i++) color[i] = -1;

        //* 連結成分ごとにBFSを開始するため、全頂点を起点候補として走査する
        for (int s = 0; s < n; s++) {
            
        	//* 既に彩色済みの頂点は属する成分が処理済みのためスキップする
        	if (color[s] != -1) continue;

        	//* BFS用のキューを用意し、始点sに色0を割り当てて探索を開始する
            Queue<Integer> q = new LinkedList<>();
            color[s] = 0;
            q.add(s);
            
            //* キューが空になるまで幅優先で探索し、隣接頂点へ交互彩色を伝搬する
            while (!q.isEmpty()) {
                int u = q.poll();
                
                //* uの全隣接vを走査し、未彩色なら反転色を割り当て、衝突なら偽を返す
                for (int v : list_adjlist.get(u)) {
                    if (color[v] == -1) {
                        color[v] = 1 - color[u];
                        q.add(v);
                    } else if (color[v] == color[u]) {
                        return false;
                    }
                }
            }
        }
        
        //* 全成分で衝突が検出されなかったため二部グラフと判定して真を返す
        return true;
	}
	
	
	// 作成したメソッドのテストを行うメソッドです。
	// このメソッドがfalseを出力した場合、解答のメソッドが正しく設計されていません。
	// ただし、falseが出力されなかったとしても正解とは限りません。
	private static void test(){
		
		//test1
		int nodenumtest1 = 4;
		int[][] ar_edgetest1 = {{0, 1}, {1, 2}, {2, 3}, {0, 3}};
		boolean test1ans1 = true;
		
		//test1-1
		ArrayList<ArrayList<Integer>> list_adjlisttest1 = getAdjListFromEdges(ar_edgetest1, nodenumtest1, false);
		//System.out.println(list_adjlisttest1);
		boolean test1res1 = false;
		test1res1 = check(list_adjlisttest1);
		//System.out.println(test1res1);
		System.out.println(test1res1 == test1ans1);
		
	}
	
	// 枝の集合から隣接リストを取得するメソッド
	private static ArrayList<ArrayList<Integer>> getAdjListFromEdges(int[][] ar_edge, int nodenum, boolean directed){
		//隣接リストを初期化（全てを空のリストにする）
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
}

