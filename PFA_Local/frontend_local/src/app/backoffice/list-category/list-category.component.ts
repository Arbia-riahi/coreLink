import { Component, OnInit } from '@angular/core';
import { CategoryService } from '../../services/category.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-list-category',
  templateUrl: './list-category.component.html',
  styleUrls: ['./list-category.component.css']
})
export class ListCategoryComponent implements OnInit {
  categories: any[] = [];  // Correction : initialiser comme un tableau vide

  constructor(private categoryService: CategoryService, private router: Router) {}

  ngOnInit() {
    this.refreshListCategories();
  }

  refreshListCategories(): void {
    this.categoryService.getCategories().subscribe(
      (response: any[]) => {  // Correction : typage explicite des données attendues
        console.log("Catégories récupérées :", response);
        this.categories = response;
      },
      (error) => {
        console.error("Erreur lors de la récupération des catégories :", error);
      }
    );
  }

  addCategory() {
    this.router.navigate(['/backoffice/addCategory']);
  }

  updateCategory(category: any) {
    console.log("ID de la catégorie à modifier :", category.id);
    this.router.navigate(['/backoffice/updateCategory', category.id]);
  }
  

  deleteCategory(id: any): void {
    if (confirm('Voulez-vous vraiment supprimer cette catégorie ?')) {
      this.categoryService.deleteCategory(id).subscribe(
        () => {
          console.log("Catégorie supprimée avec succès !");
          this.refreshListCategories();
        },
        (error) => {
          console.error("Erreur lors de la suppression :", error);
        }
      );
    }
  }
}
