import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CategoryService } from '../../services/category.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-update-category',
  templateUrl: './update-category.component.html',
  styleUrls: ['./update-category.component.css']
})
export class UpdateCategoryComponent implements OnInit {
  category: any = { nom: '' };
  categoryId!: number;

  constructor(
    private categoryService: CategoryService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.categoryId = Number(this.route.snapshot.paramMap.get('id'));
    console.log("ID récupéré :", this.categoryId);

    if (!this.categoryId || isNaN(this.categoryId)) {
      console.error("ID invalide, retour à la liste des catégories.");
      this.router.navigate(['/backoffice/listCategory']);
    } else {
      this.getCategory();
    }
  }

  getCategory(): void {
    this.categoryService.getCategoryById(this.categoryId).subscribe(
      (data) => {
        this.category = data;
      },
      (error) => {
        console.error("Erreur lors de la récupération de la catégorie :", error);
        Swal.fire("Erreur", "Impossible de charger la catégorie.", "error");
        this.router.navigate(['/backoffice/listCategory']);
      }
    );
  }

  updateCategory(): void {
    if (this.category && this.category.nom && this.category.nom.trim()) {
      this.categoryService.updateCategory(this.categoryId, this.category).subscribe(
        () => {
          Swal.fire({
            title: 'Succès !',
            text: 'Catégorie mise à jour avec succès.',
            icon: 'success',
            confirmButtonText: 'OK'
          }).then(() => {
            this.router.navigate(['/backoffice/listCategory']);
          });
        },
        (error) => {
          console.error("Erreur lors de la mise à jour de la catégorie :", error);
          Swal.fire("Erreur", "Une erreur est survenue lors de la mise à jour.", "error");
        }
      );
    } else {
      Swal.fire("Erreur", "Le nom de la catégorie ne peut pas être vide.", "error");
    }
  }
}
