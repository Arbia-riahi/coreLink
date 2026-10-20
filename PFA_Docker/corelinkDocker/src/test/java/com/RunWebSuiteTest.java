package com;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(
        key = Constants.PLUGIN_PROPERTY_NAME,
        value = "pretty, html:target/reports/cucumber-report.html, com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
)
@ConfigurationParameter(
        key = Constants.SNIPPET_TYPE_PROPERTY_NAME,
        value = "camelcase"
)

@ConfigurationParameter(
        key = Constants.FILTER_TAGS_PROPERTY_NAME,
        value = "@domain.logout"
            
)
/*
@ConfigurationParameter(
        key = Constants.FILTER_TAGS_PROPERTY_NAME,
        value = "@archive_projet or @create_projet or @update_projet or "
              + "@domain.authorization or @domain.login or @domain.logout or "
              + "@domain.actualites or @Modifier_personal_Informations or "
              + "@archivage_user or @create_user or @serachAndPaginationUser or "
              + "@update_user or @creation_tache or @MotDePasseOublie"
)*/

public class RunWebSuiteTest {}
