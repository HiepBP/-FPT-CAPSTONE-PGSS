using Capstone.Sdk;
using Capstone.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Web.Http.Description;

namespace Capstone.Controllers
{
    [RoutePrefix("api/ParkingLots")]
    public class ParkingLotsController : ApiController
    {
        [HttpGet]
        [Route("GetParkingLotsByCarParkId/{carParkId}")]
        [ResponseType(typeof(List<ParkingLotWithItemViewModel>))]
        public IHttpActionResult GetParkingLotsByCarParkId(int carParkId)
        {
            try
            {
                ParkingLotApi parkingLotApi = new ParkingLotApi();
                var listParkingLot = parkingLotApi.GetParkingLotsByCarParkId(carParkId);
                return Json(new
                {
                    result = listParkingLot,
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
