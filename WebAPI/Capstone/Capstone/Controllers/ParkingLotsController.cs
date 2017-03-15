using Capstone.Models;
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
        [ResponseType(typeof(List<ParkingLotViewModel>))]
        public IHttpActionResult GetParkingLotsByCarParkId(int carParkId)
        {
            try
            {
                ParkingLotApi parkingLotApi = new ParkingLotApi();
                var listParkingLot = parkingLotApi.GetParkingLotsByCarParkId(carParkId);
                return Json(new ResultModel
                {
                    obj = listParkingLot,
                    success = true,
                });
            }
            catch (Exception ex)
            {
                return Json(new ResultModel
                {
                    success = false,
                });
            }
        }

        [HttpGet]
        [Route("GetParkingLotsByAreaId/{areaId}")]
        [ResponseType(typeof(List<ParkingLotViewModel>))]
        public IHttpActionResult GetParkingLotsByAreaId(int areaId)
        {
            try
            {
                ParkingLotApi parkingLotApi = new ParkingLotApi();
                var listParkingLot = parkingLotApi.GetParkingLotsByAreaId(areaId);
                return Json(new ResultModel
                {
                    obj = listParkingLot,
                    success = true,
                });
            }
            catch (Exception ex)
            {
                return Json(new ResultModel
                {
                    success = false,
                });
            }
        }

        [HttpPost]
        [Route("UpdateName")]
        public IHttpActionResult UpdateName(ParkingLotUpdateViewModel model)
        {
            try
            {
                var parkingLotApi = new ParkingLotApi();
                parkingLotApi.UpdateName(model);
                return Json(new ResultModel
                {
                    success = true,
                });
            }
            catch (Exception ex)
            {
                return Json(new ResultModel
                {
                    success = false,
                });
            }
        }

        [HttpPost]
        [Route("UpdateStatus")]
        public IHttpActionResult UpdateStatus(ParkingLotUpdateViewModel model)
        {
            try
            {
                var parkingLotApi = new ParkingLotApi();
                parkingLotApi.UpdateStatus(model);
                return Json(new ResultModel
                {
                    message = "Cập nhập thành công",
                    success = true,
                });
            }
            catch (Exception ex)
            {
                return Json(new ResultModel
                {
                    message = "Có lỗi xảy ra, vui lòng liên hệ admin",
                    success = false,
                });
            }
        }

        [HttpPost]
        [Route("UpdateMultiStatus")]
        public IHttpActionResult UpdateMultiStatus(IEnumerable<ParkingLotUpdateViewModel> model)
        {
            try
            {
                var parkingLotApi = new ParkingLotApi();
                foreach (var item in model)
                {
                    parkingLotApi.UpdateStatus(item);
                }
                return Json(new ResultModel
                {
                    message = "Cập nhập thành công",
                    success = true,
                });
            }
            catch (Exception ex)
            {
                return Json(new ResultModel
                {
                    message = "Có lỗi xảy ra, vui lòng liên hệ admin",
                    success = false,
                });
            }
        }
    }
}
