package com.stepDefinition.gestionProfils;

import com.facadeComponent.SweetAlerts;
import com.hooks.SetUp;
import com.pageObjects.authentification.LoginPage;
import com.pageObjects.gestionProfils.ForgotResetPasswordPage;
import com.utils.MailsacUtil;
import com.utils.dataReader.PropertyReader;

import io.cucumber.java.en.*;

public class ForgotResetPasswordSteps {
	private final ForgotResetPasswordPage page = new ForgotResetPasswordPage(SetUp.getDriver());
	private final PropertyReader config = new PropertyReader();
	private final SweetAlerts sweetAlert = new SweetAlerts(SetUp.getDriver());
	private final LoginPage loginPage = new LoginPage(SetUp.getDriver());

	@Given("je suis sur la page de connexion")
	public void loginPage() {
		loginPage.login(config.getProperties("url_login"));
	}

	@And("qu’il clique sur {string}")
	@And("que je clique sur {string}")
	public void clickLinkForgotPassword(String btn) {
		page.clickByLabel(btn);

	}

	@And("que je saisis l’adresse e-mail {string}")
	public void emailwrite(String e) {
		e = config.getProperties("mailsac.test.email");
		page.saisirEmail(e);

	}
	
	@And("que je saisis l’adresse e-mail inexistante {string}")
	public void emailwriteNotValid(String e) {
		 page.saisirEmail(e);

	}

	// password input
	@And("qu’il saisit un nouveau mot de passe {string}")
	public void nvPasswordwrite(String p) {
		p = config.getProperties("newPassword");
		page.saisirNvPassword(p);

	}

	@And("qu’il saisit un nouveau mot de passe faible {string}")
	public void nvPasswordFaiblewrite(String p) {
		page.saisirNvPassword(p);

	}

	@And("qu’il saisit un premier nouveau mot de passe {string}")
	public void nvPassword(String p) {
		page.saisirNvPassword(p);

	}

	// confirm password input
	@And("qu’il confirme le mot de passe dans le champ {string}")
	public void nvPasswordConfirmwrite(String p) {
		p = config.getProperties("newPassword");
		page.saisirConfirmNvPassword(p);

	}

	@And("qu’il confirme le mot de passe faible dans le champ {string}")
	public void nvPasswordConfirFaiblemwrite(String p) {
		page.saisirConfirmNvPassword(p);

	}

	@And("qu’il saisit autre mot de passe dans {string}")
	public void nvPasswordConfir(String p) {
		page.saisirConfirmNvPassword(p);

	}

	@Then("un message de confirmation apparaît avec le titre {string} et le message {string}")
	public void sweetalerteConfirmation(String title, String msg) {
		sweetAlert.assertAlertTemplateForEmail(title, msg);
	}

	@Then("une alerte de succès s affiche avec le titre {string} et le message indique {string}")
	public void sweetalertesucces(String title, String msg) {
		sweetAlert.assertAlert(title, msg, true);
	}

	@Then("une alerte d avertissement s affiche avec le titre {string} et le message indique {string}")
	public void sweetalerteAvertissement(String title, String msg) {
		sweetAlert.assertAlert(title, msg, true);
	}
	
	@Then("une alerte d erreur s affiche avec le titre {string} et le message indique {string}")
	public void sweetalerteError(String title, String msg) {
		sweetAlert.assertAlert(title, msg, true);
	}

	@And("il clique sur le lien de réinitialisation dans cet email")
	public void cliquerSurLienDeResetDepuisMailsac() {
		String apiKey = config.getProperties("mailsac.api.key");
		String email = config.getProperties("mailsac.test.email");

		MailsacUtil.getResetLink(apiKey, email).ifPresentOrElse(link -> SetUp.getDriver().get(link), () -> {
			throw new RuntimeException("Aucun email de réinitialisation trouvé dans Mailsac.");
		});
	}
	
	@And("je souhaite sur le lien de réinitialisation déja expiré")
	public void cliquerSurLienExpire() {
	    String apiKey = config.getProperties("mailsac.api.key");
	    String email  = config.getProperties("mailsac.test.email");

	    MailsacUtil.getResetLink(apiKey, email).ifPresentOrElse(link -> {
	        // 👉 On ajoute simplement le flag forceExpired à la fin du lien
	        String expiredLink = link + "forceExpired=true";

	        // 👉 On ouvre le lien dans le navigateur
	        SetUp.getDriver().get(expiredLink);

	    }, () -> {
	        throw new RuntimeException("Aucun email de réinitialisation trouvé dans Mailsac.");
	    });
	}


}
