using AutoMapper;
using Capstone.Models.Entities;
using Capstone.Models.Entities.Services;
using Capstone.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http;
using System.Web.Mvc;
using System.Web.Optimization;
using System.Web.Routing;

namespace Capstone
{
    public class WebApiApplication : System.Web.HttpApplication
    {
        protected void Application_Start()
        {
            AreaRegistration.RegisterAllAreas();
            GlobalConfiguration.Configure(WebApiConfig.Register);
            FilterConfig.RegisterGlobalFilters(GlobalFilters.Filters);
            RouteConfig.RegisterRoutes(RouteTable.Routes);
            BundleConfig.RegisterBundles(BundleTable.Bundles);

            Capstone.ApiEndpoint.Entry(this.AdditionalMapperConfig);
        }

        public void AdditionalMapperConfig(IMapperConfiguration config)
        {
            config.CreateMap<CarParkWithAmountEntities, CarParkWithAmount>();
            config.CreateMap<Transaction, TransactionCustomViewModel>();
                //.ForMember(q => q.ParkingLot, opt => opt.MapFrom(q => q.ParkingLot));
        }
    }
}
