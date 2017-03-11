using AutoMapper.QueryableExtensions;
using Capstone.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Capstone.Sdk
{
    public partial class ParkingLotApi
    {
        public IEnumerable<ParkingLotViewModel> GetParkingLotsByCarParkId(int carParkId)
        {
            var result = this.BaseService.GetParkingLotsByCarParkId(carParkId).ProjectTo<ParkingLotViewModel>(this.AutoMapperConfig).AsEnumerable();
            return result;
        }

        public IEnumerable<ParkingLotViewModel> GetParkingLotsByAreaId(int areaId)
        {
            var result = this.BaseService.GetParkingLotsByAreaId(areaId).ProjectTo<ParkingLotViewModel>(this.AutoMapperConfig).AsEnumerable();
            return result;
        }
    }
}