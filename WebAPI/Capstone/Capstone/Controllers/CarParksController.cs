using Capstone.Sdk;
using Capstone.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace Capstone.Controllers
{
    public class CarParksController : ApiController
    {
        [HttpGet]
        [Route("api/CarParks/GetActive")]
        public IHttpActionResult GetActive()
        {
            try
            {
                CarParkApi carParkApi = new CarParkApi();
                var listCarPark = carParkApi.GetActive();
                return Json(new
                {
                    result = listCarPark,
                    success = true,
                });
            }
            catch (Exception ex)
            {
                return Json(new
                {
                    success = false,
                });
            }
        }

        [HttpGet]
        public IHttpActionResult Get()
        {
            try
            {
                CarParkApi carParkApi = new CarParkApi();
                var listCarPark = carParkApi.Get();
                return Json(new
                {
                    result = listCarPark,
                    success = true,
                });
            }
            catch (Exception ex)
            {
                return Json(new
                {
                    success = false,
                });
            }
        }

        [HttpGet]
        public IHttpActionResult Get(int id)
        {
            CarParkApi carParkApi = new CarParkApi();
            try
            {
                var carPark = carParkApi.Get(id);
                return Json(new
                {
                    result = carPark,
                    success = true,
                });
            }
            catch (Exception ex)
            {
                return Json(new
                {
                    success = false,
                });
            }
        }

        [HttpPost]
        public IHttpActionResult Post([FromBody]CarParkViewModel model)
        {
            CarParkApi carParkApi = new CarParkApi();
            try
            {
                carParkApi.Create(model);
                return Json(new
                {
                    success = true,
                });
            }
            catch (Exception ex)
            {
                return Json(new
                {
                    success = false,
                });
            }
        }
    }
}