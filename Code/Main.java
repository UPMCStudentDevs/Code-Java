import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

public class Main {
	/*
	 * group=false n'affiche pas les groupes de TD 
	 */
	public static final boolean GROUP = true;
	public static final int NB_UE = 5;

	public static void main(String[] args) {
		//cr�e les UE et assigne les TD
		ini();

		System.out.println("nb possibilit�s th�oriques " + UE.nbTheo(NB_UE, GROUP));

		//r�sultats stock�s dans un arbre
		TreeSet<UE[]> tree = new TreeSet<>(UE.getComparator(GROUP));

		//calcul combinaisons possibles
		enumeration(tree);
		
		System.out.println("nb combinaisons possibles " + tree.size());
		
		//affiche liste
		Iterator<UE[]> it = tree.iterator();
		while (it.hasNext()) {
			UE[] tabU = it.next();
			UE.checkOk(tabU, NB_UE);//debug
			System.out.println(UE.toString(tabU, GROUP));
		}
	}

	
	public static void enumeration(TreeSet<UE[]> tree) {
		enumeration(tree,new UE[NB_UE],0,0);
	}
	
	/**
	 * Enum�re toutes les combinaisons possibles de {@value #NB_UE} UE
	 * @param tree Les r�sulats sont stock�s dans un arbre de UE[NB_UE], l'ordonnement est d�fini par l'arbre lors de sa cr�ation
	 * @param tab tableau auxiliaire de NB_UE cases
	 * @param indMin mettre � 0 lors de l'appel
	 * @param profondeur mettre � 0 lors de l'appel
	 * La complexit� th�orique doit �tre O(nb_groupe ^ taille_tableau * log(nb_groupe ^ taille_tableau))
	 * Apr�s ex�cution, avec une taille d'arbre donn�e n, on sait qu'on a eu une complexit� de
	 * n*log(n)		pour les ajouts dans le tableau
	 * n*nb_groupe 	(au maximum) pour �num�rer les combinaisons
	 */
	private static void enumeration(TreeSet<UE[]> tree, UE[] tab, int indMin, int profondeur){		
		//inutile de tester avec les (length - profondeur) derni�res UE
		//car il ne resterait pas assez d'UE pour remplir tab
		final int indMax = UE.listeUE.size() + profondeur - tab.length + 1;

		//�num�ration avec toutes les UE disponibles
		for (int i=indMin; i<indMax; i++) {
			UE u = UE.listeUE.get(i);

			if(u.isDisponible()) {
				tab[profondeur]=u;

				//appel terminal stocke tab dans arbre 
				if(profondeur == tab.length - 1) {
					u.ajoutChaqueGroupeDispo(tree, tab);
				}
				else {
					for(int index : u.getIndicesLibres()) {
						//rend indisponibles les cr�neaux incompatibles
						LinkedList<HashSet<Creneau>> l = u.prendre(index);

						UE.checkOk(tab,profondeur);//debug

						//appel r�cursif
						enumeration(tree, tab, i+1, profondeur+1);

						//re-rend disponibles les cr�neaux pr�c�dents
						u.undo(l);
					}
				}
			}
		}
	}


	private static void ini() {
		int[] id = 		{5,	8,	10,	26,	2,	18,	24,	27,	17,	19,	25,	21,	12,	22,	20,		10,	24,	24,	2,	10,	20,		25,	17,	8,	26,	18,	22,		12,	17,	2,	17,	26,	10,	5,	21,	24,		12,	2,	10,	27,	19,	17,	2,		8,	2,	12,	27,	2,	5};
		int[] jour = 	{0,	0,	0,	0,	1,	1,	2,	2,	3,	3,	3,	3,	4,	4,	4,		0,	0,	0,	0,	0,	0,		1,	1,	1,	1,	1,	1,		2,	2,	2,	2,	2,	2,	2,	2,	2,		3,	3,	3,	3,	3,	3,	3,		4,	4,	4,	4,	4,	4};
		int[] heure = 	{0,	1,	2,	3,	0,	1,	1,	2,	0,	1,	2,	3,	1,	2,	3,		0,	0,	1,	2,	3,	3,		1,	1,	2,	3,	3,	3,		0,	0,	0,	2,	2,	2,	2,	3,	3,		0,	1,	1,	2,	3,	3,	3,		0,	0,	2,	2,	2,	2};
		int[] groupe = 	{0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,	0,		1,	4,	1,	2,	2,	1,		1,	1,	1,	3,	1,	1,		1,	2,	1,	4,	1,	3,	1,	1,	2,		2,	3,	4,	1,	1,	3,	4,		2,	5,	3,	2,	6,	2};
		UE.genererListeUE(id, jour, heure, groupe);
	}
}
