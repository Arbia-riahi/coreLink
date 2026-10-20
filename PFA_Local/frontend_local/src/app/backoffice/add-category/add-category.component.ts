import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CategoryService } from '../../services/category.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-add-category',
  templateUrl: './add-category.component.html',
  styleUrls: ['./add-category.component.css'] // Correction ici
})
export class AddCategoryComponent {
  category: any = { nom: '' }; // Correction : initialisation correcte

  constructor(private categoryService: CategoryService, private router: Router) {}

  addCategory(): void {
    if (this.category && this.category.nom && this.category.nom.trim()) {
      this.categoryService.addCategory(this.category).subscribe(
        (data) => {
          Swal.fire({
            title: 'Succès !',
            text: 'Catégorie ajoutée avec succès.',
            icon: 'success',
            confirmButtonText: 'OK'
          }).then(() => {
            this.router.navigate(['/backoffice/listCategory']);
          });
        },
        (error) => {
          console.error("Erreur lors de l'ajout de la catégorie :", error);
          Swal.fire("Erreur", "Une erreur est survenue lors de l'ajout de la catégorie.", "error");
        }
      );
    } else {
      Swal.fire("Erreur", "Le nom de la catégorie ne peut pas être vide.", "error");
    }
  }
}
