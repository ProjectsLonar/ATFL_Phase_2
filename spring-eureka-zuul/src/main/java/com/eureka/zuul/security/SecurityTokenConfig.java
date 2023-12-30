package com.eureka.zuul.security;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity // Enable security config. This annotation denotes config for spring security.
public class SecurityTokenConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private JwtConfig jwtConfig;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.cors().and().csrf().disable()
				// make sure we use stateless session; session won't be used to store user's
				// state.
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				// handle an authorized attempts
				.exceptionHandling()
				.authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()
				// Add a filter to validate the tokens with every request
				.addFilterAfter(new JwtTokenAuthenticationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class)
				// authorization requests config
				.authorizeRequests()

				// For PREVERIFIED & admin & superadmin
				// For RETAILER & admin & superadmin
				.antMatchers("/uam" + "/users" + "/sendOTP/**").permitAll()
				.antMatchers("/uam" + "/version" + "/getALLAPIVersion/**").permitAll()
				.antMatchers("/zuul" + "/user" + "/getInactiveUsers/**").permitAll()
				.antMatchers("/uam" + "/users" + "/getInactiveUsers/**").permitAll()
				.antMatchers("/uam" + "/users" + "/isMobileSupportedVersion/**").permitAll()
				.antMatchers("/api/auth/login/**").permitAll().antMatchers(HttpMethod.POST, jwtConfig.getUri()).permitAll()
				
				// Need to give specific roles
				.antMatchers("/uam" + "/fileupload" + "/uploadfile/**").permitAll()
				.antMatchers("/uam" + "/fileupload" + "/getuploadedfiles/**").permitAll()
				.antMatchers("/uam" + "/fileupload" + "/deleteuplodedfile/**").permitAll()
				
				.antMatchers("/uam" + "/salepersons" + "/verifySalesOfficer/**").hasAnyRole("PREVERIFIED","SALESOFFICER")
				.antMatchers("/uam" + "/salepersons" + "/verifyAreaHead/**").hasAnyRole("PREVERIFIED","AREAHEAD")
				
				.antMatchers("/uam" + "/outlets" + "/getById/**").hasAnyRole("PREVERIFIED", "RETAILER", "ADMIN", "SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/uam" + "/outlets" + "/update/**").hasAnyRole("PREVERIFIED", "RETAILER", "ADMIN", "SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/uam" + "/outlets" + "/verifyOutlet/**").hasAnyRole("PREVERIFIED","RETAILER", "ADMIN", "SUPERADMIN","SALESOFFICER","AREAHEAD")
				//.antMatchers("/uam" + "/outlets" + "/confirmOutletVarify/**").hasAnyRole("PREVERIFIED", "RETAILER", "ADMIN", "SUPERADMIN")
				.antMatchers("/uam" + "/outlets" + "/outletSearchByStore/**").hasAnyRole("DISTRIBUTOR", "RETAILER", "ADMIN", "SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/uam" + "/outlets" + "/searchAllOutlet/**").hasAnyRole("DISTRIBUTOR", "RETAILER", "ADMIN", "SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/uam" + "/outlets" + "/allOutletSearch/**").hasAnyRole("DISTRIBUTOR", "RETAILER", "ADMIN", "SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/uam" + "/outlets" + "/searchOutletByStoreName/**").hasAnyRole("DISTRIBUTOR", "RETAILER", "ADMIN", "SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/uam" + "/outlets" + "/getOutlet/**").hasAnyRole("DISTRIBUTOR", "RETAILER","SALES", "ADMIN", "SUPERADMIN","SALESOFFICER","AREAHEAD")
				
				.antMatchers("/uam" + "/outlets" + "/getSelectedOutlet/**").hasAnyRole("DISTRIBUTOR", "RETAILER","SALES", "ADMIN", "SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/uam" + "/users" + "/setSelectedOutlet/**").hasAnyRole("DISTRIBUTOR", "RETAILER","SALES", "ADMIN", "SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/uam" + "/users" + "/delete/**").hasAnyRole("PREVERIFIED","DISTRIBUTOR", "RETAILER","SALES", "ADMIN", "SUPERADMIN","SALESOFFICER","AREAHEAD")
				
				// For SALES & admin & superadmin
				.antMatchers("/uam" + "/salepersons" + "/getById/**").hasAnyRole("PREVERIFIED", "SALES", "ADMIN", "SUPERADMIN")
				.antMatchers("/uam" + "/salepersons" + "/update/**").hasAnyRole("PREVERIFIED", "SALES", "ADMIN", "SUPERADMIN")
				.antMatchers("/uam" + "/salepersons" + "/verifyEmployee/**").hasAnyRole("PREVERIFIED","SALES", "ADMIN", "SUPERADMIN")
				.antMatchers("/uam" + "/salepersons" + "/searchSalesPerson/**").hasAnyRole("PREVERIFIED", "SALES", "ADMIN", "SUPERADMIN")
				// For DISTRIBUTOR & admin & superadmin
				.antMatchers("/uam" + "/distributor" + "/update/**").hasAnyRole("PREVERIFIED", "DISTRIBUTOR", "ADMIN", "SUPERADMIN")
				.antMatchers("/uam" + "/distributor" + "/getById/**").hasAnyRole("PREVERIFIED", "DISTRIBUTOR", "ADMIN", "SUPERADMIN")
				.antMatchers("/uam" + "/distributor" + "/verifyDistributor/**").hasAnyRole("PREVERIFIED", "DISTRIBUTOR", "ADMIN", "SUPERADMIN")
				// For DISTRIBUTOR & admin & superadmin
				.antMatchers("/uam" + "/org" + "/getById/**").hasAnyRole("PREVERIFIED", "ADMIN", "SUPERADMIN")
				.antMatchers("/uam" + "/org" + "/verifyOrganisation/**").hasAnyRole("PREVERIFIED",  "ADMIN", "SUPERADMIN")
				.antMatchers("/uam" + "/users" + "/update/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/uam" + "/users" + "/getPenddingApprovalByDistributorId/**").hasAnyRole("DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/uam" + "/users" + "/changeUserStatus/**").hasAnyRole("PREVERIFIED","DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/uam" + "/users" + "/getUserById/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				
				
				.antMatchers("/uam" + "/users" + "/savePersonalDetails/**").hasAnyRole( "PREVERIFIED","DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD","SALESOFFICER","AREAHEAD")
				.antMatchers("/uam" + "/users" + "/getPersonaldetailsById/**").hasAnyRole( "PREVERIFIED","DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/uam" + "/users" + "/saveAddress/**").hasAnyRole("PREVERIFIED","DISTRIBUTOR","ADMIN","SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/uam" + "/users" + "/getAddressDetailsById/**").hasAnyRole( "PREVERIFIED","DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/uam" + "/users" + "/activeProfile/**").hasAnyRole( "PREVERIFIED","DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/uam" + "/users" + "/getAllUserByDistributorId/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/uam" + "/users" + "/uploadProfilePic/**").hasAnyRole( "PREVERIFIED","DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/uam" + "/promotion" + "/uploadPromotion/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/uam" + "/promotion" + "/getPromotionData/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/uam" + "/promotion" + "/deletePromotionData/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/uam" + "/promotion" + "/editPromotion/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				
				 .antMatchers("/uam" + "/outlets" + "/getAllUserMastData/**").hasAnyRole("DISTRIBUTOR", "ADMIN", "SUPERADMIN","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				 .antMatchers("/uam" + "/outlets" + "/getAllOutletType/**").hasAnyRole("DISTRIBUTOR", "ADMIN", "SUPERADMIN","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				 .antMatchers("/uam" + "/outlets" + "/getAllOutletChannel/**").hasAnyRole("DISTRIBUTOR", "ADMIN", "SUPERADMIN","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				 .antMatchers("/uam" + "/outlets" + "/createOutlet/**").hasAnyRole("DISTRIBUTOR", "ADMIN", "SUPERADMIN","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				 .antMatchers("/uam" + "/outlets" + "/getPriceListAgainstDistributor/**").hasAnyRole("DISTRIBUTOR", "ADMIN", "SUPERADMIN","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")			 
				 .antMatchers("/uam" + "/users" + "/saveRecentSearchId/**").hasAnyRole("DISTRIBUTOR", "ADMIN", "SUPERADMIN","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				 .antMatchers("/uam" + "/users" + "/getUserAllMasterDataById/**").hasAnyRole("PREVERIFIED","DISTRIBUTOR", "ADMIN", "SALES", "SUPERADMIN","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				 .antMatchers("/uam" + "/salepersons" + "/getSalesPersonsForDistributor/**").hasAnyRole("PREVERIFIED","DISTRIBUTOR","SALES", "ADMIN","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				 .antMatchers("/uam" + "/users" + "/getUsersList/**").hasAnyRole("PREVERIFIED","DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				
				.antMatchers("/master" + "/products" + "/searchProduct/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/products" + "/getdatatable/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/products" + "/findPricelistByCategoryId/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/products" + "/findProductbyStatus/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/products" + "/findBySubCategoryId/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/products" + "/findByCategoryId/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/products" + "/findByOrgId/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/products" + "/findAllProduct/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/products" + "/getMostOrderProduct/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/products" + "/getProductByProdId/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/products" + "/getProductByCatIdOrgIdDisIdOutId/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/products" + "/searchProdByCatIdOrgIdDisIdOutId/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/products" + "/getOutlet/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/products" + "/getProduct/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/products" + "/getInStockProduct/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/products" + "/getOutOfStockProduct/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				
				.antMatchers("/master" + "/dashboard" + "/statusWiseOrdersCount/**").hasAnyRole( "DISTRIBUTOR", "ADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/dashboard" + "/categoryRevenueDistribution/**").hasAnyRole( "DISTRIBUTOR", "ADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/dashboard" + "/dailySales/**").hasAnyRole( "DISTRIBUTOR", "ADMIN","SALES","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/dashboard" + "/monthlySales/**").hasAnyRole( "DISTRIBUTOR", "ADMIN","SALES","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/dashboard" + "/dailySalesExcel/**").hasAnyRole( "DISTRIBUTOR", "ADMIN","SALES","SALESOFFICER","AREAHEAD")
				
				.antMatchers("/master" + "/categories" + "/getCategory/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/categories" + "/findCategoryBy/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/categories" +"/findCategoryByStatus/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/categories" +"/searchCategory/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/subCategories" + "/findSubCategoryByorgId/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/subCategories" + "/findSubCategorybyStatus/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/pricelists" + "/getById/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/pricelists" +"/getPriceListByOrgId/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				
				.antMatchers("/master" + "/privacypolicy" +"/readPrivacyPolicy/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/faq" +"/aboutUsAndFaqDetails/**").hasAnyRole( "PREVERIFIED", "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				
				.antMatchers("/master" + "/privacypolicy" +"/privacypolicy/**").hasAnyRole( "PREVERIFIED", "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/privacypolicy" +"/termsAndConditions/**").hasAnyRole( "PREVERIFIED", "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				
				.antMatchers("/master" + "/reports" +"/getCategoryDetails/**").hasAnyRole("PREVERIFIED", "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/reports" +"/getOutletStatus/**").hasAnyRole("PREVERIFIED", "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/reports" +"/searchOutlets/**").hasAnyRole("PREVERIFIED", "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/reports" +"/searchProduct/**").hasAnyRole("PREVERIFIED", "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/reports" +"/getRegion/**").hasAnyRole("PREVERIFIED", "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/reports" +"/searchSalesPerson/**").hasAnyRole("PREVERIFIED", "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/reports" +"/searchDistributor/**").hasAnyRole("PREVERIFIED", "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/reports" +"/generateDistributorReport/**").hasAnyRole("PREVERIFIED", "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/reports" +"/generateOutletReport/**").hasAnyRole("PREVERIFIED", "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/reports" +"/generateProductReport/**").hasAnyRole("PREVERIFIED", "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/reports" +"/generateRegionWiseSalesReport/**").hasAnyRole("PREVERIFIED", "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				.antMatchers("/master" + "/reports" +"/generateSalesPersonReport/**").hasAnyRole("PREVERIFIED", "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				
				.antMatchers("/master" + "/imageUpload" +"/upload/**").hasAnyRole("PREVERIFIED", "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				
				// master test product api
				 .antMatchers("/master" + "/testProducts" + "/saveProduct/**").hasAnyRole("DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN")
				 .antMatchers("/master" + "/testProducts" + "/deleteByProductId/**").hasAnyRole("DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN")
				 .antMatchers("/master" + "/testProducts" + "/getAllProducts/**").hasAnyRole("DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN")
				 .antMatchers("/master" + "/testProducts" + "/updateProductDetails/**").hasAnyRole("DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN")
				 .antMatchers("/master" + "/testProducts" + "/getAllState/**").hasAnyRole("DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN")
				 .antMatchers("/master" + "/testProducts" + "/getAllCategory/**").hasAnyRole("DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN")
				 .antMatchers("/master" + "/testProducts" + "/getAllSubcategoryAgainstCatId/**").hasAnyRole("DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN")
				 
				/* //------Cart---- */
				 .antMatchers("/cart" + "/ltsoheaders" + "/save/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				 .antMatchers("/cart" + "/ltsoheaders" + "/getById/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				 .antMatchers("/cart" + "/ltsoheaders" + "/findByOutletId/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				 
				 .antMatchers("/cart" + "/ltsoheaders" + "/getAllOrderByOutletId/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				 .antMatchers("/cart" + "/ltsoheaders" + "/getAllOrderByDistributorId/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				 .antMatchers("/cart" + "/ltsoheaders" + "/getAllOrderBySaleId/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				 .antMatchers("/cart" + "/ltsoheaders" + "/delete/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				
				 .antMatchers("/cart" + "/ltSoLines" + "/save/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				 .antMatchers("/cart" + "/ltSoLines" + "/getById/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				 //.antMatchers("/cart" + "/ltSoLines" + "/update/**").permitAll()
				 .antMatchers("/cart" + "/ltSoLines" + "/delete/**").hasAnyRole( "DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				 
				 .antMatchers("/cart" + "/ltSoLines" + "/addToCart/**").hasAnyRole("DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				 .antMatchers("/cart" + "/ltSoLines" + "/placeOrder/**").hasAnyRole("DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				 
				 .antMatchers("/cart" + "/ltsoheaders" + "/getOrder/**").hasAnyRole("DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN")
				 .antMatchers("/cart" + "/ltsoheaders" + "/saveOrder/**").hasAnyRole("DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				 
				 .antMatchers("/cart" + "/ltsoheaders" + "/getAllInprocessOrder/**").hasAnyRole("DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				 
				 .antMatchers("/cart" + "/ltsoheaders" + "/getOrderCancellationReason/**").hasAnyRole("DISTRIBUTOR", "ADMIN", "SALES","RETAILER","SUPERADMIN","SALESOFFICER","AREAHEAD")
				 .antMatchers("/uam" + "/salepersons" + "/systemAdministrator/**").hasAnyRole("PREVERIFIED","SALES", "ADMIN", "SUPERADMIN")
				 
				// ANONYMOUS

				// Any other request must be authenticated
				.anyRequest().authenticated();
	}

	@Bean
	public JwtConfig jwtConfig() {
		return new JwtConfig();
	}
}