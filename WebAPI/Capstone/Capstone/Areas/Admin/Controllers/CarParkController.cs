using Capstone.Sdk;
using Capstone.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace Capstone.Areas.Admin.Controllers
{
    public class CarParkController : ApiController
    {
        [HttpPost]
        public IHttpActionResult Create()
        {
            CarParkViewModel vm = new CarParkViewModel()
            {
                Active = true,
                Address = "123",
                Email = "123@gmail.com",
                Lat = "0",
                Lon = "0",
                Name = "abc",
                Phone = "123",
                Description = "abc",
            };
            try
            {
                CarParkApi carParkApi = new CarParkApi();
                carParkApi.Create(vm);
                return Json(new
                {
                    msg = "Create Success"
                });
            }
            catch (Exception ex)
            {
                return Json(new
                {
                    msg = ex
                });
            }
        }
    }
}
