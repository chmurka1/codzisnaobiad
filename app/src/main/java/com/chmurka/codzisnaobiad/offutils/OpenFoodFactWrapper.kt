@file:Suppress("unused", "PropertyName")

package com.chmurka.codzisnaobiad.offutils

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.apache.commons.lang3.ObjectUtils

/* adapted after https://github.com/openfoodfacts/openfoodfacts-java/ */

class SelectedImageItem {
    private val en: String? = null
    private val fr: String? = null
    private val pl: String? = null
    val url: String? get() = ObjectUtils.firstNonNull(en, fr, pl)
}

class SelectedImage {
    val display: SelectedImageItem? = null
    val small: SelectedImageItem? = null
    val thumb: SelectedImageItem? = null
}

class SelectedImages {
    val front: SelectedImage? = null
    val ingredients: SelectedImage? = null
    val nutrition: SelectedImage? = null
}

class Source {
    val fields: Array<String>? = null
    val id: String? = null
    val images: Array<String>? = null
    val import_t: Long = 0
    val manufacturer: String? = null
    val name: String? = null
    val url: String? = null
}

class LanguagesCodes {
    val en: String? = null
    val fr: String? = null
    val pl: String? = null
}


class NutrientLevels {
    val fat: String? = null
    val salt: String? = null
    val saturated_fat: String? = null
    val sugars: String? = null
}

class Nutriments {
    val calcium = 0f
    val calcium_value = 0f
    val calcium_100g = 0f
    val calcium_serving = 0f
    val calcium_unit: String? = null
    val carbohydrates = 0f
    val carbohydrates_value = 0f
    val carbohydrates_100g = 0f
    val carbohydrates_serving = 0f
    val carbohydrates_unit: String? = null
    val carbon_footprint_from_known_ingredientsProduct = 0f
    val carbon_footprint_from_known_ingredients_100g = 0f
    val carbon_footprint_from_known_ingredients_serving = 0f
    val cholesterol = 0f
    val cholesterol_value = 0f
    val cholesterol_100g = 0f
    val cholesterol_serving = 0f
    val cholesterol_unit: String? = null
    val energy = 0
    @SerializedName("energy-kcal")
    val energy_kcal = 0
    @SerializedName("energy-kj")
    val energy_kj = 0
    val energy_value = 0
    @SerializedName("energy-kcal_value")
    val energy_kcal_value = 0
    @SerializedName("energy-kj_value")
    val energy_kj_value = 0
    val energy_100g = 0
    @SerializedName("energy-kcal_100g")
    val energy_kcal_100g = 0
    @SerializedName("energy-kj_100g")
    val energy_kj_100g = 0
    val energy_serving = 0
    @SerializedName("energy-kcal_serving")
    val energy_kcal_serving = 0
    @SerializedName("energy-kj_serving")
    val energy_kj_serving = 0
    val energy_unit: String? = null
    @SerializedName("energy-kcal_unit")
    val energy_kcal_unit: String? = null
    @SerializedName("energy-kj_unit")
    val energy_kj_unit: String? = null
    val fat = 0f
    val fat_value = 0f
    val fat_100g = 0f
    val fat_serving = 0f
    val fat_unit: String? = null
    val fiber = 0f
    val fiber_value = 0f
    val fiber_100g = 0f
    val fiber_serving = 0f
    val fiber_unit: String? = null
    @SerializedName("fruits-vegetables-nuts_100g_estimate")
    val fruits_vegetables_nuts_estimate_from_ingredients_100g = 0f
    val iron = 0f
    val iron_value = 0f
    val iron_100g = 0f
    val iron_serving = 0f
    val iron_unit: String? = null
    val nova_group = 0f
    val nova_group_100g = 0f
    val nova_group_serving = 0f
    val proteins = 0f
    val proteins_value = 0f
    val proteins_100g = 0f
    val proteins_serving = 0f
    val proteins_unit: String? = null
    val salt = 0f
    val salt_value = 0f
    val salt_100g = 0f
    val salt_serving = 0f
    val salt_unit: String? = null
    val saturated_fat = 0f
    val saturated_fat_value = 0f
    val saturated_fat_100g = 0f
    val saturated_fat_serving = 0f
    val saturated_fat_unit: String? = null
    val sodium = 0f
    val sodium_value = 0f
    val sodium_100g = 0f
    val sodium_serving = 0f
    val sodium_unit: String? = null
    val sugars = 0f
    val sugars_value = 0f
    val sugars_100g = 0f
    val sugars_serving = 0f
    val sugars_unit: String? = null
    val trans_fat = 0f
    val trans_fat_value = 0f
    val trans_fat_100g = 0f
    val trans_fat_serving = 0f
    val trans_fat_unit: String? = null
    val vitamin_a = 0f
    val vitamin_a_value = 0f
    val vitamin_a_100g = 0f
    val vitamin_a_serving = 0f
    val vitamin_a_unit: String? = null
    val vitamin_c = 0f
    val vitamin_c_value = 0f
    val vitamin_c_100g = 0f
    val vitamin_c_serving = 0f
    val vitamin_c_unit: String? = null
    val vitamin_d = 0f
    val vitamin_d_value = 0f
    val vitamin_d_100g = 0f
    val vitamin_d_serving = 0f
    val vitamin_d_unit: String? = null
}

class Ingredient {
    val from_palm_oil: String? = null
    val id: String? = null
    val origin: String? = null
    val percent: String? = null
    val rank = 0
    val text: String? = null
    val vegan: String? = null
    val vegetarian: String? = null
}

class Product {
    //val images: Images? = null
    val ingredients: Array<Ingredient>? = null
    val languages_codes: LanguagesCodes? = null
    val nutrient_levels: NutrientLevels? = null
    val nutriments: Nutriments? = null
    val selected_images: SelectedImages? = null
    val sources: Array<Source>? = null
    val additives_n = 0
    val additives_old_n = 0
    val additives_original_tags: Array<String>? = null
    val additives_old_tags: Array<String>? = null
    val additives_prev_original_tags: Array<String>? = null
    val additives_debug_tags: Array<String>? = null
    val additives_tags: Array<String>? = null
    val allergens: String? = null
    val allergens_from_ingredients: String? = null
    val allergens_from_user: String? = null
    val allergens_hierarchy: Array<String>? = null
    val allergens_lc: String? = null
    val allergens_tags: Array<String>? = null
    val amino_acids_prev_tags: Array<String>? = null
    val amino_acids_tags: Array<String>? = null
    val brands: String? = null
    val brands_debug_tags: Array<String>? = null
    val brands_tags: Array<String>? = null
    val carbon_footprint_percent_of_known_ingredients: String? = null
    val carbon_footprint_from_known_ingredients_debug: String? = null
    val categories: String? = null
    val categories_hierarchy: Array<String>? = null
    val categories_lc: String? = null
    val categories_properties_tags: Array<String>? = null
    val categories_tags: Array<String>? = null
    val checkers_tags: Array<String>? = null
    val cities_tags: Array<String>? = null
    val code: String? = null
    val codes_tags: Array<String>? = null
    val compared_to_category: String? = null
    val complete = 0
    val completed_t: Long = 0
    val completeness = 0f
    val conservation_conditions: String? = null
    val countries: String? = null
    val countries_hierarchy: Array<String>? = null
    val countries_lc: String? = null
    val countries_debug_tags: Array<String>? = null
    val countries_tags: Array<String>? = null
    val correctors_tags: Array<String>? = null
    val created_t: Long = 0
    val creator: String? = null
    val data_quality_bugs_tags: Array<String>? = null
    val data_quality_errors_tags: Array<String>? = null
    val data_quality_info_tags: Array<String>? = null
    val data_quality_tags: Array<String>? = null
    val data_quality_warnings_tags: Array<String>? = null
    val data_sources: String? = null
    val data_sources_tags: Array<String>? = null
    val debug_param_sorted_langs: Array<String>? = null
    val editors_tags: Array<String>? = null
    val emb_codes: String? = null
    val emb_codes_debug_tags: Array<String>? = null
    val emb_codes_orig: String? = null
    val emb_codes_tags: Array<String>? = null
    val entry_dates_tags: Array<String>? = null
    val expiration_date: String? = null
    val expiration_date_debug_tags: Array<String>? = null
    val fruits_vegetables_nuts_100g_estimate = 0
    val generic_name: String? = null
    val id: String? = null
    val _id: String? = null
    val image_front_small_url: String? = null
    val image_front_thumb_url: String? = null
    val image_front_url: String? = null
    val image_ingredients_url: String? = null
    val image_ingredients_small_url: String? = null
    val image_ingredients_thumb_url: String? = null
    val image_nutrition_small_url: String? = null
    val image_nutrition_thumb_url: String? = null
    val image_nutrition_url: String? = null
    val image_small_url: String? = null
    val image_thumb_url: String? = null
    val image_url: String? = null
    val informers_tags: Array<String>? = null
    val ingredients_analysis_tags: Array<String>? = null
    val ingredients_debug: Array<String>? = null
    val ingredients_from_or_that_may_be_from_palm_oil_n = 0
    val ingredients_from_palm_oil_tags: Array<String>? = null
    val ingredients_from_palm_oil_n = 0
    val ingredients_hierarchy: Array<String>? = null
    val ingredients_ids_debug: Array<String>? = null
    val ingredients_n = 0
    val ingredients_n_tags: Array<String>? = null
    val ingredients_original_tags: Array<String>? = null
    val ingredients_tags: Array<String>? = null
    val ingredients_text: String? = null
    val ingredients_text_debug: String? = null
    val ingredients_text_with_allergens: String? = null
    val ingredients_that_may_be_from_palm_oil_n = 0
    val ingredients_that_may_be_from_palm_oil_tags: Array<String>? = null
    val interface_version_created: String? = null
    val interface_version_modified: String? = null
    val keywords: Array<String>? = null
    val known_ingredients_n = 0
    val labels: String? = null
    val labels_hierarchy: Array<String>? = null
    val labels_lc: String? = null
    val labels_prev_hierarchy: Array<String>? = null
    val labels_prev_tags: Array<String>? = null
    val labels_tags: Array<String>? = null
    val labels_debug_tags: Array<String>? = null
    val lang: String? = null
    val lang_debug_tags: Array<String>? = null
    val languages_hierarchy: Array<String>? = null
    val languages_tags: Array<String>? = null
    val last_edit_dates_tags: Array<String>? = null
    val last_editor: String? = null
    val last_image_dates_tags: Array<String>? = null
    val last_image_t: Long = 0
    val last_modified_by: String? = null
    val last_modified_t: Long = 0
    val lc: String? = null
    val link: String? = null
    val link_debug_tags: Array<String>? = null
    val manufacturing_places: String? = null
    val manufacturing_places_debug_tags: Array<String>? = null
    val manufacturing_places_tags: Array<String>? = null
    val max_imgid: String? = null
    val minerals_prev_tags: Array<String>? = null
    val minerals_tags: Array<String>? = null
    val misc_tags: Array<String>? = null
    val net_weight_unit: String? = null
    val net_weight_value: String? = null
    val nutrition_data_per: String? = null
    val nutrition_score_warning_no_fruits_vegetables_nuts = 0
    val no_nutrition_data: String? = null
    val nova_group: String? = null
    val nova_groups: String? = null
    val nova_group_debug: String? = null
    val nova_group_tags: Array<String>? = null
    val nova_groups_tags: Array<String>? = null
    val nucleotides_prev_tags: Array<String>? = null
    val nucleotides_tags: Array<String>? = null
    val nutrient_levels_tags: Array<String>? = null
    val nutrition_data: String? = null
    val nutrition_data_per_debug_tags: Array<String>? = null
    val nutrition_data_prepared: String? = null
    val nutrition_data_prepared_per: String? = null
    val nutrition_grades: String? = null
    val nutrition_score_beverage = 0
    val nutrition_score_debug: String? = null
    val nutrition_score_warning_no_fiber = 0
    val nutrition_grades_tags: Array<String>? = null
    val origins: String? = null
    val origins_debug_tags: Array<String>? = null
    val origins_tags: Array<String>? = null
    val other_information: String? = null
    val other_nutritional_substances_tags: Array<String>? = null
    val packaging: String? = null
    val packaging_debug_tags: Array<String>? = null
    val packaging_tags: Array<String>? = null
    val photographers_tags: Array<String>? = null
    val pnns_groups_1: String? = null
    val pnns_groups_2: String? = null
    val pnns_groups_1_tags: Array<String>? = null
    val pnns_groups_2_tags: Array<String>? = null
    val popularity_key: Long = 0
    val producer_version_id: String? = null
    val product_name: String? = null
    val product_quantity: String? = null
    val purchase_places: String? = null
    val purchase_places_debug_tags: Array<String>? = null
    val purchase_places_tags: Array<String>? = null
    val quality_tags: Array<String>? = null
    val quantity: String? = null
    val quantity_debug_tags: Array<String>? = null
    val recycling_instructions_to_discard: String? = null
    val rev = 0
    val serving_quantity: String? = null
    val serving_size: String? = null
    val serving_size_debug_tags: Array<String>? = null
    val sortkey: Long = 0
    val states: String? = null
    val states_hierarchy: Array<String>? = null
    val states_tags: Array<String>? = null
    val stores: String? = null
    val stores_debug_tags: Array<String>? = null
    val stores_tags: Array<String>? = null
    val traces: String? = null
    val traces_from_ingredients: String? = null
    val traces_hierarchy: Array<String>? = null
    val traces_debug_tags: Array<String>? = null
    val traces_from_user: String? = null
    val traces_lc: String? = null
    val traces_tags: Array<String>? = null
    val unknown_ingredients_n = 0
    val unknown_nutrients_tags: Array<String>? = null
    val update_key: String? = null
    val vitamins_prev_tags: Array<String>? = null
    val vitamins_tags: Array<String>? = null
}

class ProductResponse {
    val product: Product? = null
    val code: String? = null
    val status = 0
    val status_verbose: String? = null
}

fun openFoodFactsApiCall(
    barcode: String,
    callback: (ProductResponse) -> Unit,
    onError: () -> Unit,
    context: Context
) {
    val queue = Volley.newRequestQueue(context)
    val url = "https://world.openfoodfacts.org/api/v0/product/${barcode}.json"

    val stringRequest = StringRequest(
        Request.Method.GET, url,
        { response ->
            val gson = Gson()
            var obj: ProductResponse = gson.fromJson(response, ProductResponse::class.java)
            callback(obj)
        },
        { onError() })

    queue.add(stringRequest)
}

