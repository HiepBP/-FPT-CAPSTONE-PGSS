using AutoMapper.QueryableExtensions;
using Capstone.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Capstone.Sdk
{
    public partial class AreaApi
    {
        public IEnumerable<AreaViewModel> GetAreaByCarParkId(int carParkId)
        {
            return this.BaseService.GetAreaByCarParkId(carParkId)
                .ProjectTo<AreaViewModel>(this.AutoMapperConfig)
                .ToList();
        }
    }
}