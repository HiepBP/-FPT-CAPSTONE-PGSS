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
        public IEnumerable<ParkingLotWithItemViewModel> GetParkingLotsByCarParkId(int carParkId)
        {
            var result = this.BaseService.GetParkingLotsByCarParkId(carParkId).ProjectTo<ParkingLotWithItemViewModel>(this.AutoMapperConfig).AsEnumerable();
            return result;
        }
    }
}