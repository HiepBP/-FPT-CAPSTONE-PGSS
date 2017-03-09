using Capstone.Sdk;
using Capstone.ViewModels;
using System;
using System.Collections.Generic;
using System.Device.Location;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Web.Http.Description;

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
        public IHttpActionResult Create([FromBody]CarParkViewModel model)
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

        [HttpGet]
        [Route("api/CarParks/GetEmptyAmount")]
        public IHttpActionResult GetEmptyAmount(int id)
        {
            CarParkApi carParkApi = new CarParkApi();
            AreaApi areaApi = new AreaApi();
            try
            {
                var emptyAmount = carParkApi.GetEmptyAmount(id);
                return Json(new
                {
                    result = emptyAmount,
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
        [Route("api/CarParks/GetCoordinateCarPark")]
        public IHttpActionResult GetCoordinateCarPark()
        {
            CarParkApi carParkApi = new CarParkApi();
            try
            {
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

        /// <summary>
        /// 
        /// </summary>
        /// <param name="lat">Lattitude</param>
        /// <param name="lon">Longtitude</param>
        /// <param name="numberOfCarPark">Number of car park you want to get</param>
        /// <returns></returns>
        [HttpGet]
        [Route("api/CarParks/GetCoordinateNearestCarPark/{lat}/{lon}/{numberOfCarPark}")]
        [ResponseType(typeof(List<CarParkGeoJson>))]
        public IHttpActionResult GetCoordinateNearestCarPark(double lat, double lon, int numberOfCarPark)
        {
            CarParkApi carParkApi = new CarParkApi();
            var coord = new GeoCoordinate(lat, lon);
            try
            {
                var listCarPark = carParkApi.GetCoordinatesWithEmptyAmount();
                var nearest = listCarPark.Select(q => new
                {
                    Carpark = q.CarPark,
                    EmptyAmount = q.EmptyAmount,
                    Geo = new GeoCoordinate(double.Parse(q.CarPark.Lat), double.Parse(q.CarPark.Lon)),
                    Distance = new GeoCoordinate(double.Parse(q.CarPark.Lat), double.Parse(q.CarPark.Lon)).GetDistanceTo(coord),
                })
                    .OrderBy(q => q.Geo.GetDistanceTo(coord))
                    .Take(numberOfCarPark);
                return Json(new
                {
                    result = nearest,
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
        [Route("api/CarParks/GetCoordinateNearestCarParkByRange/{lat}/{lon}/{range}")]
        [ResponseType(typeof(List<CarParkGeoJson>))]
        public IHttpActionResult GetCoordinateNearestCarParkByRange(double lat, double lon, double range)
        {
            CarParkApi carParkApi = new CarParkApi();
            var coord = new GeoCoordinate(lat, lon);
            //change from kilometers to meters
            range = range * 1000;
            try
            {
                var listCarPark = carParkApi.GetCoordinatesWithEmptyAmount();
                var nearest = listCarPark.Select(q => new
                {
                    Carpark = q.CarPark,
                    EmptyAmount = q.EmptyAmount,
                    Geo = new GeoCoordinate(double.Parse(q.CarPark.Lat), double.Parse(q.CarPark.Lon)),
                    Distance = new GeoCoordinate(double.Parse(q.CarPark.Lat), double.Parse(q.CarPark.Lon)).GetDistanceTo(coord),
                })
                    .Where(q => q.Geo.GetDistanceTo(coord) <= range);
                return Json(new
                {
                    result = nearest,
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



        private class CarParkGeoJson
        {
            public CarParkViewModel CarPark { get; set; }
            public int EmptyAmount { get; set; }
            public GeoCoordinate Geo { get; set; }
            public double Distance { get; set; }
        }
    }
}